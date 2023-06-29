package com.kurlic.chessgpt.chess

import android.util.Size
import java.io.Serializable

class SizeSeriazable: Serializable
{
    var width: Int;
    var height: Int;

    constructor(width: Int, height: Int)
    {
        this.width = width;
        this.height = height;
    }
}