package com.example.chessgpt.chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorSpace.Rgb
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Size
import android.util.SizeF
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

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
        chessBoard = ChessBoard(this)
        chessBoard.initBoard(null);
    }

    private lateinit var chessBoard: ChessBoard


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        calcChessCellSize()
        whiteFigurePaint.textSize = chessPixelSize!!.height * textCellPercentage;
        blackFigurePaint.textSize = chessPixelSize!!.height * textCellPercentage;
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                val pos:Point = getRectPosFromPix(Point(event.x.toInt(), event.y.toInt()))
                Toast.makeText(context, pos.toString(), Toast.LENGTH_SHORT).show()
                possibleMoves = chessBoard.getPossibleMoves(pos)

                return true
            }
        }
        return true
    }

    var possibleMoves: ArrayList<Point> = ArrayList()
    

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawNet(canvas)
        drawChess(canvas)
    }

    private val whiteFigureColor = Color.WHITE;
    private val blackFigureColor = Color.BLACK;

    private var whiteFigurePaint: Paint = Paint().apply {
        color = whiteFigureColor
    }
    private var blackFigurePaint: Paint = Paint().apply {
        color = blackFigureColor
    }

    private val textCellPercentage: Float = 0.6F

    private fun drawChess(canvas: Canvas?)
    {
        for(x in 0 until chessSize.width)
        {
            for (y in 0 until chessSize.height)
            {
                //chessBoard.board[x][y];
                val textRectF: RectF = calcRect(Point(x, y))
                val midPointF: PointF = PointF(
                    (textRectF.left + textRectF.right)/2,
                    (textRectF.top + textRectF.bottom)/2
                )

                if(chessBoard.board[x][y]?.isWhite == true)
                {
                    chessBoard.board[x][y]?.type?.symbol?.let { canvas?.drawText(it, midPointF.x, midPointF.y, whiteFigurePaint) }
                }
                else
                {
                    chessBoard.board[x][y]?.type?.symbol?.let { canvas?.drawText(it, midPointF.x, midPointF.y, blackFigurePaint) }
                }

            }
        }
    }

    private val chessSize: Size = Size(8, 8)
    private var chessPixelSize: SizeF? = null

    private fun calcChessCellSize() {
        val cellWidth: Float = width.toFloat() / chessSize.width
        val cellHeight: Float = height.toFloat() / chessSize.height
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

        for(x in 0 until chessSize.width)
        {
            for(y in 0 until chessSize.height)
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

    fun isValidPoint(pos: Point) : Boolean
    {
        if(pos.x < 0) return false
        if(pos.y < 0) return false
        if(pos.x >= chessSize.width) return false
        if(pos.y >= chessSize.height) return false

        return true
    }
}