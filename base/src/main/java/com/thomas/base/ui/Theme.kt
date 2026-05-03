package com.thomas.base.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

/**
 * Created by thomas on 5/3/2026.
 */

object Style {
    val t = TextStyle()

    object Text {
        val size = Size
        val family = Family
        val weight = Weight

        object Size {
            val f1 = 12.sp
            val f2 = 14.sp
            val f3 = 16.sp
            val f4 = 18.sp
            val f5 = 20.sp
            val f6 = 22.sp
            val f7 = 24.sp
            val f8 = 26.sp
            val f9 = 28.sp
            val f10 = 30.sp
        }

        object Family {
            val default = FontFamily.Default
            val sans = FontFamily.SansSerif
            val serif = FontFamily.Serif
            val monospace = FontFamily.Monospace
        }

        object Weight {
            val thin = FontWeight.Thin
            val extraLight = FontWeight.ExtraLight
            val light = FontWeight.Light
            val normal = FontWeight.Normal
            val medium = FontWeight.Medium
            val semiBold = FontWeight.SemiBold
            val bold = FontWeight.Bold
            val black = FontWeight.Black
        }
    }

    object Colors {
        val transparent = Color(0x00000000)
        val red = Color(0xFFFF0000)
        val green = Color(0xFF00FF00)
        val yellow = Color(0xFFFFFF00)
        val blue = Color(0xFF0000FF)
        val black = Color(0xFF000000)
        val white = Color(0xFFFFFFFF)
        val gray = Color(0xFF808080)
        val lightGray = Color(0xFFF0F0F0)
        val lightBlue = Color(0xFFE6F7FF)
        val lightGreen = Color(0xFFE6FFEA)
        val lightRed = Color(0xFFFFE6E6)
        val lightYellow = Color(0xFFFFE6E6)

    }
}

val text = Style.Text
val colors = Style.Colors

fun TextStyle.weight(weight: FontWeight) = copy(fontWeight = weight)
fun TextStyle.family(family: FontFamily) = copy(fontFamily = family)
fun TextStyle.size(size: TextUnit) = copy(fontSize = size)
fun TextStyle.color(color: Color) = copy(color = color)

fun TextStyle.thin() = weight(text.weight.thin)
fun TextStyle.extraLight() = weight(text.weight.extraLight)
fun TextStyle.light() = weight(text.weight.light)
fun TextStyle.normal() = weight(text.weight.normal)
fun TextStyle.medium() = weight(text.weight.medium)
fun TextStyle.semiBold() = weight(text.weight.semiBold)
fun TextStyle.bold() = weight(text.weight.bold)
fun TextStyle.blackWeight() = weight(text.weight.black)

fun TextStyle.default() = family(text.family.default)
fun TextStyle.sans() = family(text.family.sans)
fun TextStyle.serif() = family(text.family.serif)
fun TextStyle.monospace() = family(text.family.monospace)

fun TextStyle.f1() = size(text.size.f1)
fun TextStyle.f2() = size(text.size.f2)
fun TextStyle.f3() = size(text.size.f3)
fun TextStyle.f4() = size(text.size.f4)
fun TextStyle.f5() = size(text.size.f5)
fun TextStyle.f6() = size(text.size.f6)
fun TextStyle.f7() = size(text.size.f7)
fun TextStyle.f8() = size(text.size.f8)
fun TextStyle.f9() = size(text.size.f9)
fun TextStyle.f10() = size(text.size.f10)

fun TextStyle.red() = color(colors.red)
fun TextStyle.green() = color(colors.green)
fun TextStyle.blue() = color(colors.blue)
fun TextStyle.black() = color(colors.black)
fun TextStyle.white() = color(colors.white)
fun TextStyle.transparent() = color(colors.transparent)
