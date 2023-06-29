package com.kurlic.chessgpt.chess

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
import android.widget.Toast
import com.google.gson.Gson

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
    }

    private var chessBoard: ChessBoard = ChessBoard()
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

    public fun saveBoardToJson() : String
    {
        val chessBoardJson = chessBoard.toJson()

        return chessBoardJson
    }

    public fun loadBoardFromJson(jsonString: String?) : Boolean
    {
        if(jsonString != null)
        {
            val gson = Gson()
            chessBoard = gson.fromJson(jsonString, ChessBoard::class.java)
            invalidate()
            return true
        }

        chessBoard.initBoard()
        invalidate()
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                val pos:Point = getRectPosFromPix(Point(event.x.toInt(), event.y.toInt()))
                Toast.makeText(context, pos.toString(), Toast.LENGTH_SHORT).show()
                if(pos != lastClickedPos)
                {
                    if(lastClickedPos != null)
                    {
                        val canMove = chessBoard.canMove(possibleMoves, pos, lastClickedPos!!)

                        if(canMove)
                        {
                            chessBoard.move(lastClickedPos!!, pos)
                            possibleMoves = ArrayList()
                            lastClickedPos = null
                            return true
                        }
                    }
                    possibleMoves = chessBoard.getPossibleMoves(pos)
                    lastClickedPos = pos;
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

    private val whiteFigureColor = Color.WHITE;
    private val blackFigureColor = Color.BLACK;

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
                val textRectF: RectF = calcRect(Point(x, y))
                val midPointF: PointF = getRectMid(textRectF)

                if(chessBoard.board[x][y].isWhite)
                {
                    chessBoard.board[x][y].type.symbol.let { canvas?.drawText(it, midPointF.x, midPointF.y, whiteFigurePaint) }
                }
                else
                {
                    chessBoard.board[x][y].type.symbol.let { canvas?.drawText(it, midPointF.x, midPointF.y, blackFigurePaint) }
                }

            }
        }
    }

    private var chessPixelSize: SizeF? = null

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
                if(isWhite)
                {
                    canvas?.drawRect(chessRect, whitePaint);
                }
                else
                {
                    canvas?.drawRect(chessRect, blackPaint);
                }

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