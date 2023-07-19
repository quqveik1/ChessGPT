package com.kurlic.chessgpt.chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.SizeF
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.kurlic.chessgpt.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChessGraphicLoader(private val context: Context, private val chessView: ChessView) {

    companion object
    {
        private var pieceGraphics: Map<String, Bitmap> = HashMap()
        private var loadFinished = false
        private var previousPoleSize: SizeF = SizeF(0F, 0F)
    }

    private lateinit var loadingDialog: AlertDialog

    private fun showLoadingDialog() {
        loadFinished = false

        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.game_graphic_loading_dialog, null)

        builder.setView(view)
        builder.setCancelable(false) // optional, prevents user from closing dialog

        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun hideLoadingDialog()
    {
        loadFinished = true

        loadingDialog.dismiss()
        chessView.invalidate()
    }


    @SuppressLint("DiscouragedApi")
    fun loadImages(poleSize: SizeF) {
        if(poleSize != previousPoleSize)
        {
            showLoadingDialog()
            CoroutineScope(Dispatchers.IO).launch {
                val pieceTypes = ChessPieceType.values().filter { it != ChessPieceType.EMPTY }
                val colors = listOf("white", "black")
                pieceGraphics = HashMap()
                pieceGraphics = pieceTypes.flatMap { pieceType ->
                    colors.map { color ->
                        val key = "${pieceType.symbol}_$color"
                        val bitmap = getBitmapFromResourceName(context, key, poleSize)
                        key to bitmap
                    }
                }.toMap()

                previousPoleSize = SizeF(poleSize.width, poleSize.height)

                withContext(Dispatchers.Main)
                {
                    hideLoadingDialog()
                }

            }
        }
    }


    @SuppressLint("DiscouragedApi")
    private fun getBitmapFromResourceName(context: Context, resourceName: String, poleSize: SizeF): Bitmap {
        val resId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        val origBitmap = BitmapFactory.decodeResource(context.resources, resId)

        val scaledBitmap = Bitmap.createScaledBitmap(origBitmap, poleSize.width.toInt(), poleSize.height.toInt(), true)

        return  scaledBitmap
    }

    fun getGraphicBitmap(pieceType: ChessPieceType, isWhite: Boolean): Bitmap? {
        if(pieceType == ChessPieceType.EMPTY) return null
        if(!loadFinished) return null

        val color = if (isWhite) "white" else "black"
        val key = "${pieceType.symbol}_$color"
        return pieceGraphics[key] ?: throw IllegalArgumentException("No graphic for key: $key")
    }
}

