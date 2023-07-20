package com.kurlic.chessgpt.chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.text.Layout.Alignment
import android.util.AttributeSet
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.kurlic.chessgpt.gpt.GPTMove
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@DelicateCoroutinesApi
class ChessView : View{

    constructor(context: Context) : super(context)
    {
        commonConstructor()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        commonConstructor()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        commonConstructor()
    }

    private fun commonConstructor()
    {
        chessGraphicLoader = ChessGraphicLoader(context, this)
    }

     lateinit var chessGraphicLoader: ChessGraphicLoader

    var chessBoard: ChessBoard = ChessBoard()
        set(value)
        {
            field = value
            invalidate()
        }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        calcChessCellSize()
        whiteFigurePaint.textSize = chessPixelSize!!.height * textCellPercentage;
        blackFigurePaint.textSize = chessPixelSize!!.height * textCellPercentage;
    }

    var moveListener: ChessMoveListener? = null
        set(value)
        {
            field = value
            chessBoard.chessMoveListener = value
        }

    fun saveBoardToJson() : String
    {
        val chessBoardJson = chessBoard.toJson()

        return chessBoardJson
    }

    fun loadBoardFromJson(jsonString: String?) : Boolean
    {
        var res = false;
        if(jsonString != null)
        {
            val gson = Gson()
            chessBoard = gson.fromJson(jsonString, ChessBoard::class.java)
            res = true
        }
        else
        {
            chessBoard.initBoard()
            res = true
        }
        chessBoard.chessMoveListener = moveListener

        moveListener?.onArrangementMade(chessBoard)
        invalidate()
        return res
    }

    fun moveIfCan(gptMove: GPTMove) : Boolean
    {
        val startPos = Point(gptMove.sx, gptMove.sy)
        val finishPos = Point(gptMove.dx, gptMove.dy)
        val possibleMoves = chessBoard.getPossibleMoves(startPos)

        val canMove = chessBoard.canMove(possibleMoves, startPos, finishPos)

        if(canMove)
        {
            chessBoard.move(finishPos, startPos)
            return true
        }

        return false

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                val pos:Point = getRectPosFromPix(Point(event.x.toInt(), event.y.toInt()))
                if(pos != lastClickedPos)
                {
                    if(lastClickedPos != null)
                    {
                        val canMove = chessBoard.canMove(possibleMoves, pos, lastClickedPos!!)

                        if(canMove)
                        {
                            doMove(lastClickedPos!!, pos)
                            return true
                        }
                    }
                    possibleMoves = chessBoard.getPossibleMoves(pos)
                    lastClickedPos = pos
                }
                else
                {
                    possibleMoves = ArrayList()
                    lastClickedPos = null
                }

                return true
            }
        }
        return true
    }

    fun doMove(start: Point, finish: Point)
    {
        chessBoard.move(start, finish)
        possibleMoves = ArrayList()
        lastClickedPos = null
        moveListener?.onMoveMade(chessBoard)
    }

    var possibleMoves: ArrayList<Point> = ArrayList()
        set(value) {
            field = value
            invalidate()
        }
    var lastClickedPos: Point? = null
    val possibleMovesColor = Color.GREEN
    val possibleMovesRPercantage = 0.3;
    val possibleMovesPaint = Paint().apply {
        color = possibleMovesColor
    }


    private fun drawPossibleMoves(canvas: Canvas?)
    {
        val pixR = chessPixelSize!!.height * possibleMovesRPercantage

        for(i in possibleMoves)
        {
            val rectF = calcRect(i)

            val midPointF = getRectMid(rectF)

            canvas?.drawCircle(midPointF.x, midPointF.y, pixR.toFloat(), possibleMovesPaint)
        }
    }

    companion object
    {
        fun getRectMid(rect: RectF) : PointF
        {
            val midPointF: PointF = PointF(
                (rect.left + rect.right)/2,
                (rect.top + rect.bottom)/2
            )

            return midPointF
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawNet(canvas)
        drawFigures(canvas)
        drawPossibleMoves(canvas)
    }

    private val whiteFigureColor = Color.WHITE
    private val blackFigureColor = Color.BLACK

    private val chessTextSizePer = 0.3f
    private var chessTextSize: Float? = null
        set(value)
        {
            field = value;
            if(field != null)whitePaint.textSize = field!!;
            if(field != null)blackPaint.textSize = field!!;
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

    private fun drawFigures(canvas: Canvas?)
    {
        for(x in 0 until chessBoard.chessSize.width)
        {
            for (y in 0 until chessBoard.chessSize.height)
            {
                //chessBoard.board[x][y];
                val startRectF: RectF = calcRect(Point(x, y))
                val finishRectF: RectF = calcRect(Point(x + 1, y + 1))
                val bitmap = chessGraphicLoader.getGraphicBitmap(chessBoard.board[x][y].type, chessBoard.board[x][y].isWhite);

                if(bitmap != null) canvas!!.drawBitmap(bitmap, startRectF.left, startRectF.top, whiteFigurePaint)

            }
        }
    }

    private var chessPixelSize: SizeF? = null
        set(value)
        {
            field = value
            chessTextSize = field!!.height * chessTextSizePer;
            if(value != null)chessGraphicLoader.loadImages(value)
        }

    private fun calcChessCellSize() {
        val cellWidth: Float = width.toFloat() / chessBoard.chessSize.width
        val cellHeight: Float = height.toFloat() / chessBoard.chessSize.height
        chessPixelSize = SizeF(cellWidth, cellHeight)
    }

    private val whiteColor = Color.argb(255, 70, 70, 70)
    private val blackColor = Color.argb(255, 180, 180, 180)

    private val whitePaint: Paint = Paint().apply {
        color = whiteColor
    }
    private val blackPaint: Paint = Paint().apply {
        color = blackColor
    }


    private fun drawNet(canvas: Canvas?)
    {
        var isWhite: Boolean = true

        for(x in 0 until chessBoard.chessSize.width)
        {
            for(y in 0 until chessBoard.chessSize.height)
            {
                val chessRect = calcRect(Point(x, y))
                val activePaint = if (isWhite) whitePaint else blackPaint;
                val activeTextPaint = if (!isWhite) whitePaint else blackPaint;

                canvas?.drawRect(chessRect, activePaint);
                if(y == 0) canvas?.drawText((x + 65).toChar().toString(), chessRect.left, chessRect.top + chessRect.height() - activeTextPaint.textSize * 0.1f, activeTextPaint)
                if(x == 0) canvas?.drawText((8 - y).toString(), chessRect.left, chessRect.top + activeTextPaint.textSize, activeTextPaint)

                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }
    }

    private fun calcRect(pos : Point) : RectF
    {
        val ans: RectF = RectF();

        ans.left = pos.x * chessPixelSize!!.width
        ans.top = pos.y * chessPixelSize!!.height
        ans.right = (pos.x + 1) * chessPixelSize!!.width
        ans.bottom = (pos.y + 1) * chessPixelSize!!.height

        return ans
    }

    private fun getRectPosFromPix(pos : Point) : Point
    {
        val ans: Point = Point();

        ans.x = pos.x / (chessPixelSize!!.width.toInt())
        ans.y = pos.y / (chessPixelSize!!.height.toInt())

        return ans
    }


}