package com.kurlic.chessgpt.chess

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.google.gson.Gson
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.gpt.GPTMove
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.math.sqrt

class ChessView : View {
    constructor(context: Context) : super(context) {
        commonConstructor()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        commonConstructor()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        commonConstructor()
    }

    private fun commonConstructor() {
        chessGraphicLoader = ChessGraphicLoader(context, this)
    }

    lateinit var chessGraphicLoader: ChessGraphicLoader

    var chessBoard: ChessBoard = ChessBoard()
        set(value) {
            field = value
            invalidate()
        }

    var canMoveOnlyBottomSide = false

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        calcChessCellSize()
        whiteFigurePaint.textSize = chessPixelSize!!.height * textCellPercentage;
        blackFigurePaint.textSize = chessPixelSize!!.height * textCellPercentage;
    }

    var moveListener: ChessMoveListener? = null
        set(value) {
            field = value
            chessBoard.chessMoveListener = value
        }

    fun saveBoardToJson(): String {
        val chessBoardJson = chessBoard.toJson()

        return chessBoardJson
    }

    fun loadBoardFromJson(jsonString: String?, isBottomSideWhite: Boolean = true): Boolean {
        var res = false
        if (jsonString != null) {
            val gson = Gson()
            chessBoard = gson.fromJson(jsonString, ChessBoard::class.java)
            res = true
        } else {
            chessBoard.initBoard(isBottomSideWhite)
            res = true
        }
        chessBoard.chessMoveListener = moveListener

        moveListener?.onArrangementMade(chessBoard)
        invalidate()
        return res
    }

    fun moveIfCan(gptMove: GPTMove): Boolean {
        val startPos = Point(gptMove.sx, gptMove.sy)
        val finishPos = Point(gptMove.dx, gptMove.dy)
        val possibleMoves = chessBoard.getPossibleMoves(startPos)

        val canMove = chessBoard.canMove(possibleMoves, startPos, finishPos)

        if (canMove) {
            chessBoard.move(finishPos, startPos)
            return true
        }

        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!(canMoveOnlyBottomSide && chessBoard.isBottomSideWhite != chessBoard.isActiveSideWhite)) {
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    val pos: Point = getModelPosFromPix(Point(event.x.toInt(), event.y.toInt()))
                    if (pos != lastClickedPos) {
                        if (lastClickedPos != null) {
                            val canMove =
                                chessBoard.canMove(possibleMoves, pos, lastClickedPos!!)

                            if (canMove) {
                                doMove(lastClickedPos!!, pos)
                                return true
                            }
                        }
                        possibleMoves = chessBoard.getPossibleMoves(pos)
                        lastClickedPos = pos
                    } else {
                        possibleMoves = ArrayList()
                        lastClickedPos = null
                    }

                    return true
                }
            }
            return true
        }
        return false
    }

    fun doMove(start: Point, finish: Point) {
        chessBoard.move(start, finish)
        possibleMoves = ArrayList()
        lastClickedPos = null
        moveListener?.onMoveMade(chessBoard)

        startAnimation(start, finish)
    }

    var possibleMoves: ArrayList<Point> = ArrayList()
        set(value) {
            field = value
            invalidate()
        }
    var lastClickedPos: Point? = null
    val possibleMovesColor by lazy {
        context.getColor(R.color.moveCell)
    }
    val possibleMovesPaint by lazy {
        Paint().apply {
            color = possibleMovesColor
        }
    }

    private fun drawPossibleMoves(canvas: Canvas?) {
        for (i in possibleMoves) {
            val rectF = calcPixRect(i)

            canvas?.drawRect(rectF, possibleMovesPaint)
        }
    }

    companion object {
        fun getRectMid(rect: RectF): PointF {
            val midPointF: PointF = PointF(
                (rect.left + rect.right) / 2,
                (rect.top + rect.bottom) / 2
            )

            return midPointF
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (chessPixelSize == null) {
            calcChessCellSize()
        }

        drawNet(canvas)
        drawFigures(canvas)
        drawPossibleMoves(canvas)
    }

    private val whiteFigureColor = Color.WHITE
    private val blackFigureColor = Color.BLACK

    private val chessTextSizePer = 0.3f
    private var chessTextSize: Float? = null
        set(value) {
            field = value;
            if (field != null) whitePaint.textSize = field!!;
            if (field != null) blackPaint.textSize = field!!;
        }

    private var whiteFigurePaint: Paint = Paint().apply {
        color = whiteFigureColor
        textAlign = Paint.Align.CENTER
    }
    private var blackFigurePaint: Paint = Paint().apply {
        color = blackFigureColor
        textAlign = Paint.Align.CENTER
    }

    private val textCellPercentage: Float = 0.6F

    private fun drawFigures(canvas: Canvas?) {
        for (x in 0 until chessBoard.chessSize.width) {
            for (y in 0 until chessBoard.chessSize.height) {

                val pos = Point(x, y)

                val contains = animatingList.any { it.finalModelPoint == pos }

                if (!contains) drawFigure(pos, canvas)
            }
        }

        drawAnimatingFigures(canvas)
    }

    private fun drawAnimatingFigures(canvas: Canvas?) {
        for (figure in animatingList) {
            drawFigureAtCoordinates(figure.finalModelPoint, figure.currentPixPos, canvas)
        }
    }

    val animatingList = ArrayList<AnimatedFigure>()

    private val animationLength = 1000L
    private val oneCellAnimationLength = 50L

    @SuppressLint("Recycle")
    fun startAnimation(start: Point, finish: Point) {
        val pixelStart = calcPixPos(start)
        val pixelFinish = calcPixPos(finish)

        val figure = AnimatedFigure(finish)

        animatingList.add(figure)

        val animationDuration = calcAnimationTime(start, finish)

        val animatorX = ValueAnimator.ofFloat(pixelStart.x, pixelFinish.x).apply {
            duration = animationDuration
            interpolator = DecelerateInterpolator()

            addUpdateListener { animation ->
                figure.currentPixPos.x = animation.animatedValue as Float
                invalidate()
            }
        }

        val animatorY = ValueAnimator.ofFloat(pixelStart.y, pixelFinish.y).apply {
            duration = animationDuration
            interpolator = DecelerateInterpolator()

            addUpdateListener { animation ->
                figure.currentPixPos.y = animation.animatedValue as Float
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    animatingList.remove(figure)
                    invalidate()
                }
            })
        }

        AnimatorSet().apply {
            playTogether(animatorX, animatorY)
            start()
        }
    }

    private fun calcAnimationTime(start: Point, finish: Point): Long {
        val deltaX = finish.x - start.x
        val deltaY = finish.y - start.y

        val ans = sqrt((deltaX * deltaX + deltaY * deltaY).toDouble()) * oneCellAnimationLength

        return ans.toLong()
    }

    private fun drawFigureAtCoordinates(pos: Point, posPix: PointF, canvas: Canvas?) {
        val bitmap = chessGraphicLoader.getGraphicBitmap(chessBoard.board[pos.x][pos.y].type, chessBoard.board[pos.x][pos.y].isWhite);

        if (bitmap != null) canvas!!.drawBitmap(bitmap, posPix.x, posPix.y, whiteFigurePaint)
    }

    private fun drawFigure(pos: Point, canvas: Canvas?) {
        val rectF = calcPixRect(pos)

        val pixPos = PointF(rectF.left, rectF.top)

        drawFigureAtCoordinates(pos, pixPos, canvas)
    }

    private var chessPixelSize: SizeF? = null
        set(value) {
            field = value
            chessTextSize = field!!.height * chessTextSizePer;
            if (value != null) chessGraphicLoader.loadImages(value)
        }
        get() {
            if (field == null) {
                field = SizeF(scaleX, scaleY)
            }
            return field
        }

    private fun calcChessCellSize() {
        val cellWidth: Float = width.toFloat() / chessBoard.chessSize.width
        val cellHeight: Float = height.toFloat() / chessBoard.chessSize.height
        chessPixelSize = SizeF(cellWidth, cellHeight)
    }

    private val whiteColor = context.getColor(R.color.whiteCell)
    private val blackColor = context.getColor(R.color.blackCell)

    private val whitePaint: Paint = Paint().apply {
        color = whiteColor
    }
    private val blackPaint: Paint = Paint().apply {
        color = blackColor
    }

    private fun drawNet(canvas: Canvas?) {
        var isWhite: Boolean = true

        for (x in 0 until chessBoard.chessSize.width) {
            for (y in 0 until chessBoard.chessSize.height) {
                val chessRect = calcPixRect(Point(x, y))
                val activePaint = if (isWhite) whitePaint else blackPaint;
                val activeTextPaint = if (!isWhite) whitePaint else blackPaint;

                canvas?.drawRect(chessRect, activePaint);
                if (y == 0) canvas?.drawText((x + 65).toChar().toString(), chessRect.left, chessRect.top + chessRect.height() - activeTextPaint.textSize * 0.1f, activeTextPaint)
                if (x == 0) canvas?.drawText((8 - y).toString(), chessRect.left, chessRect.top + activeTextPaint.textSize, activeTextPaint)

                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
    }

    private fun calcPixRect(pos: Point): RectF {
        val ans: RectF = RectF();

        ans.left = pos.x * chessPixelSize!!.width
        ans.top = pos.y * chessPixelSize!!.height
        ans.right = (pos.x + 1) * chessPixelSize!!.width
        ans.bottom = (pos.y + 1) * chessPixelSize!!.height

        return ans
    }

    private fun calcPixPos(pos: Point): PointF {
        val ans = PointF();

        ans.x = pos.x * chessPixelSize!!.width
        ans.y = pos.y * chessPixelSize!!.height

        return ans
    }

    private fun getModelPosFromPix(pos: Point): Point {
        val ans: Point = Point();

        ans.x = pos.x / (chessPixelSize!!.width.toInt())
        ans.y = pos.y / (chessPixelSize!!.height.toInt())

        return ans
    }
}