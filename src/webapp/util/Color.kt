package webapp.util

import webapp.core.asString

/**
 * Helper class with default colors
 * Also contains default color values for Material Design
 *
 * @see https://material.io/guidelines/style/color.html
 */
class Color private constructor() {

    companion object {

        /**
         * Gets R channel value of color
         */
        fun rValue(color : String) : Int {
            val pColor = color.filter { it in ADDITIONAL_COLOR_CHARS }
            if (pColor.startsWith(RGBA_PATTERN)) {
                return pColor.substring(4).split(",")[0].toInt()
            }
            if (pColor.startsWith(RGB_PATTERN)) {
                return pColor.substring(3).split(",")[0].toInt()
            }
            if (color.startsWith(SHARP_PATTERN)) {
                return hexToInt(pColor.substring(1, 3))
            }
            return 0
        }

        /**
         * Gets G channel value of color
         */
        fun gValue(color : String) : Int {
            val pColor = color.filter { it in ADDITIONAL_COLOR_CHARS }
            if (pColor.startsWith(RGBA_PATTERN)) {
                return pColor.substring(4).split(",")[1].toInt()
            }
            if (pColor.startsWith(RGB_PATTERN)) {
                return pColor.substring(3).split(",")[1].toInt()
            }
            if (color.startsWith(SHARP_PATTERN)) {
                return hexToInt(pColor.substring(3, 5))
            }
            return 0
        }

        /**
         * Gets B channel value of color
         */
        fun bValue(color : String) : Int {
            val pColor = color.filter { it in ADDITIONAL_COLOR_CHARS }
            if (pColor.startsWith(RGBA_PATTERN)) {
                return pColor.substring(4).split(",")[2].toInt()
            }
            if (pColor.startsWith(RGB_PATTERN)) {
                return pColor.substring(3).split(",")[2].toInt()
            }
            if (color.startsWith(SHARP_PATTERN)) {
                return hexToInt(pColor.substring(5, 7))
            }
            return 0
        }

        /**
         * Gets A channel value of color
         */
        fun aValue(color : String) : Int {
            val pColor = color.filter { it in HEX_CHARS + ADDITIONAL_COLOR_CHARS }
            if (pColor.startsWith(RGBA_PATTERN)) {
                return pColor.substring(4).split(",")[3].toInt()
            }
            return 0
        }

        /**
         * Gets text color for background color
         */
        fun getTextColor(color : String) : String {
            val sum = listOf(this::rValue, this::gValue, this::bValue).map { it(color) }.sum()
            if (sum > 128 * 3) { // background is light
                return rgba(0, 0, 0, DEFAULT_TEXT_ALPHA)
            } else { // background is dark
                return rgba(255, 255, 255, DEFAULT_TEXT_ALPHA)
            }
        }

        /**
         * Gets a hex string value of color
         *
         * @param r red channel value [0-255]
         * @param g green channel value [0-255]
         * @param b blue channel value [0-255]
         *
         * @return String like '#FFFFFF'
         */
        fun rgbToHex(r : Int, g : Int, b : Int) : String {
            return SHARP_PATTERN + listOf(r, g, b).map { Color.intToHex(it)}.asString()
        }

        /**
         * Gets a string value of color
         *
         * @param r red channel value [0-255]
         * @param g green channel value [0-255]
         * @param b blue channel value [0-255]
         *
         * @return String like 'rgb(255, 255, 255)'
         */
        fun rgb(r : Int, g : Int, b : Int) : String {
            return "$RGB_PATTERN($r, $g, $b)"
        }

        /**
         * Gets a string value of color
         *
         * @param r red channel value [0-255]
         * @param g green channel value [0-255]
         * @param b blue channel value [0-255]
         * @param a alpha channel value [0-255]
         *
         * @return String like 'rgb(255, 255, 255, 1.0)'
         */
        fun rgba(r : Int, g : Int, b : Int, a : Int) : String {
            return "$RGBA_PATTERN($r, $g, $b, ${a.toFloat() / 255})"
        }

        /**
         * Gets a string value of color
         *
         * @param r red channel value [0-255]
         * @param g green channel value [0-255]
         * @param b blue channel value [0-255]
         * @param a alpha channel value [0-1]
         *
         * @return String like 'rgb(255, 255, 255, 1.0)'
         */
        fun rgba(r : Int, g : Int, b : Int, a : Float) : String {
            return "$RGBA_PATTERN($r, $g, $b, $a)"
        }

        /**
         * Gets hex value of Integer
         *
         * @param value channel value [0-255]
         *
         * @return hex value of channel [00-FF]
         */
        private fun intToHex(value : Int) : String {

            return listOf(0xF0, 0x0F)
                    .map { value and it }
                    .zip(arrayOf(4, 0))
                    .map { it.first.ushr(it.second) }
                    .map { HEX_CHARS[it] }
                    .asString()
        }

        /**
         * Gets Int value of hex char
         *
         * @param value channel value [0-255]
         *
         * @return hex value of channel [00-FF]
         */
        private fun hexCharValue(char : Char) = HEX_CHARS.indexOf(char, ignoreCase = true)

        /**
         * Gets Int value of hex string
         *
         * @param value hex string without '#'
         *
         * @return hex value of channel [00-FF]
         */
        private fun hexToInt(value : String) : Int {
            var result = 0
            for (c in value) {
                result *= 16
                result += hexCharValue(c)
            }
            return result
        }

        /**
         * Checks that value is color
         */
        fun isColor(value : String) : Boolean {
            // TODO: use regex
            if (value.startsWith(RGB_PATTERN)) return true
            if (value.startsWith(RGBA_PATTERN)) return true
            if (value.startsWith(SHARP_PATTERN)) return true
            return false
        }

        /**
         * RGBA color pattern
         */
        private const val RGBA_PATTERN = "rgba"

        /**
         * RGB color pattern
         */
        private const val RGB_PATTERN = "rgb"

        /**
         * # color pattern
         */
        private const val SHARP_PATTERN = "#"

        /**
         * Hex possible chars
         */
        private const val HEX_CHARS = "0123456789ABCDEF"

        /**
         * Additional color chars. used to parse color values
         */
        private const val ADDITIONAL_COLOR_CHARS = HEX_CHARS + "," + SHARP_PATTERN + RGBA_PATTERN

        /**
         * Default alpha value of text color
         */
        private const val DEFAULT_TEXT_ALPHA = 0.87f

        // Red colors
        const val RED_50 = "#FFEBEE"
        const val RED_100 = "#FFCDD2"
        const val RED_200 = "#EF9A9A"
        const val RED_300 = "#E57373"
        const val RED_400 = "#EF5350"
        const val RED_500 = "#F44336"
        const val RED_600 = "#E53935"
        const val RED_700 = "#D32F2F"
        const val RED_800 = "#C62828"
        const val RED_900 = "#B71C1C"
        const val RED_A100 = "#FF8A80"
        const val RED_A200 = "#FF5252"
        const val RED_A400 = "#FF1744"
        const val RED_A700 = "#D50000"

        // Pink colors
        const val PINK_50 = "#FCE4EC"
        const val PINK_100 = "#F8BBD0"
        const val PiINK_200 = "#F48FB1"
        const val PINK_300 = "#F06292"
        const val PINK_400 = "#EC407A"
        const val PINK_500 = "#E91E63"
        const val PINK_600 = "#D81B60"
        const val PINK_700 = "#C2185B"
        const val PINK_800 = "#AD1457"
        const val PINK_900 = "#880E4F"
        const val PINK_A100 = "#FF80AB"
        const val PINK_A200 = "#FF4081"
        const val PINK_A400 = "#F50057"
        const val PINK_A700 = "#C51162"

        // Purple colors
        const val Purple_50 = "#F3E5F5"
        const val Purple_100 = "#E1BEE7"
        const val Purple_200 = "#CE93D8"
        const val Purple_300 = "#BA68C8"
        const val Purple_400 = "#AB47BC"
        const val Purple_500 = "#9C27B0"
        const val Purple_600 = "#8E24AA"
        const val Purple_700 = "#7B1FA2"
        const val Purple_800 = "#6A1B9A"
        const val Purple_900 = "#4A148C"
        const val Purple_A100 = "#EA80FC"
        const val Purple_A200 = "#E040FB"
        const val Purple_A400 = "#D500F9"
        const val Purple_A700 = "#AA00FF"

        // Purple colors
        const val PURPLE_50 = "#F3E5F5"
        const val PURPLE_100 = "#E1BEE7"
        const val PURPLE_200 = "#CE93D8"
        const val PURPLE_300 = "#BA68C8"
        const val PURPLE_400 = "#AB47BC"
        const val PURPLE_500 = "#9C27B0"
        const val PURPLE_600 = "#8E24AA"
        const val PURPLE_700 = "#7B1FA2"
        const val PURPLE_800 = "#6A1B9A"
        const val PURPLE_900 = "#4A148C"
        const val PURPLE_A100 = "#EA80FC"
        const val PURPLE_A200 = "#E040FB"
        const val PURPLE_A400 = "#D500F9"
        const val PURPLE_A700 = "#AA00FF"

        // Deep Purple colors
        const val DEEP_PURPLE_50 = "#EDE7F6"
        const val DEEP_PURPLE_100 = "#D1C4E9"
        const val DEEP_PURPLE_200 = "#B39DDB"
        const val DEEP_PURPLE_300 = "#9575CD"
        const val DEEP_PURPLE_400 = "#7E57C2"
        const val DEEP_PURPLE_500 = "#673AB7"
        const val DEEP_PURPLE_600 = "#5E35B1"
        const val DEEP_PURPLE_700 = "#512DA8"
        const val DEEP_PURPLE_800 = "#4527A0"
        const val DEEP_PURPLE_900 = "#311B92"
        const val DEEP_PURPLE_A100 = "#B388FF"
        const val DEEP_PURPLE_A200 = "#7C4DFF"
        const val DEEP_PURPLE_A400 = "#651FFF"
        const val DEEP_PURPLE_A700 = "#6200EA"

        // Indigo colors
        const val INDIGO_50 = "#E8EAF6"
        const val INDIGO_100 = "#C5CAE9"
        const val INDIGO_200 = "#9FA8DA"
        const val INDIGO_300 = "#7986CB"
        const val INDIGO_400 = "#5C6BC0"
        const val INDIGO_500 = "#3F51B5"
        const val INDIGO_600 = "#3949AB"
        const val INDIGO_700 = "#303F9F"
        const val INDIGO_800 = "#283593"
        const val INDIGO_900 = "#1A237E"
        const val INDIGO_A100 = "#8C9EFF"
        const val INDIGO_A200 = "#536DFE"
        const val INDIGO_A400 = "#3D5AFE"
        const val INDIGO_A700 = "#304FFE"

        // Blue Text
        const val BLUE_50 = "#E3F2FD"
        const val BLUE_100 = "#BBDEFB"
        const val BLUE_200 = "#90CAF9"
        const val BLUE_300 = "#64B5F6"
        const val BLUE_400 = "#42A5F5"
        const val BLUE_500 = "#2196F3"
        const val BLUE_600 = "#1E88E5"
        const val BLUE_700 = "#1976D2"
        const val BLUE_800 = "#1565C0"
        const val BLUE_900 = "#0D47A1"
        const val BLUE_A100 = "#82B1FF"
        const val BLUE_A200 = "#448AFF"
        const val BLUE_A400 = "#2979FF"
        const val BLUE_A700 = "#2962FF"

        // Light Blue Text
        const val LIGHT_BLUE_50 = "#E1F5FE"
        const val LIGHT_BLUE_100 = "#B3E5FC"
        const val LIGHT_BLUE_200 = "#81D4FA"
        const val LIGHT_BLUE_300 = "#4FC3F7"
        const val LIGHT_BLUE_400 = "#29B6F6"
        const val LIGHT_BLUE_500 = "#03A9F4"
        const val LIGHT_BLUE_600 = "#039BE5"
        const val LIGHT_BLUE_700 = "#0288D1"
        const val LIGHT_BLUE_800 = "#0277BD"
        const val LIGHT_BLUE_900 = "#01579B"
        const val LIGHT_BLUE_A100 = "#80D8FF"
        const val LIGHT_BLUE_A200 = "#40C4FF"
        const val LIGHT_BLUE_A400 = "#00B0FF"
        const val LIGHT_BLUE_A700 = "#0091EA"

        // Cyan Text
        const val CYAN_50 = "#E0F7FA"
        const val CYAN_100 = "#B2EBF2"
        const val CYAN_200 = "#80DEEA"
        const val CYAN_300 = "#4DD0E1"
        const val CYAN_400 = "#26C6DA"
        const val CYAN_500 = "#00BCD4"
        const val CYAN_600 = "#00ACC1"
        const val CYAN_700 = "#0097A7"
        const val CYAN_800 = "#00838F"
        const val CYAN_900 = "#006064"
        const val CYAN_A100 = "#84FFFF"
        const val CYAN_A200 = "#18FFFF"
        const val CYAN_A400 = "#00E5FF"
        const val CYAN_A700 = "#00B8D4"

        // Teal colors
        const val TEAL_50 = "#E0F2F1"
        const val TEAL_100 = "#B2DFDB"
        const val TEAL_200 = "#80CBC4"
        const val TEAL_300 = "#4DB6AC"
        const val TEAL_400 = "#26A69A"
        const val TEAL_500 = "#009688"
        const val TEAL_600 = "#00897B"
        const val TEAL_700 = "#00796B"
        const val TEAL_800 = "#00695C"
        const val TEAL_900 = "#004D40"
        const val TEAL_A100 = "#A7FFEB"
        const val TEAL_A200 = "#64FFDA"
        const val TEAL_A400 = "#1DE9B6"
        const val TEAL_A700 = "#00BFA5"

        // Green colors
        const val GREEN_50 = "#E8F5E9"
        const val GREEN_100 = "#C8E6C9"
        const val GREEN_200 = "#A5D6A7"
        const val GREEN_300 = "#81C784"
        const val GREEN_400 = "#66BB6A"
        const val GREEN_500 = "#4CAF50"
        const val GREEN_600 = "#43A047"
        const val GREEN_700 = "#388E3C"
        const val GREEN_800 = "#2E7D32"
        const val GREEN_900 = "#1B5E20"
        const val GREEN_A100 = "#B9F6CA"
        const val GREEN_A200 = "#69F0AE"
        const val GREEN_A400 = "#00E676"
        const val GREEN_A700 = "#00C853"

        // Light Green colors
        const val LIGHT_GREEN_50 = "#F1F8E9"
        const val LIGHT_GREEN_100 = "#DCEDC8"
        const val LIGHT_GREEN_200 = "#C5E1A5"
        const val LIGHT_GREEN_300 = "#AED581"
        const val LIGHT_GREEN_400 = "#9CCC65"
        const val LIGHT_GREEN_500 = "#8BC34A"
        const val LIGHT_GREEN_600 = "#7CB342"
        const val LIGHT_GREEN_700 = "#689F38"
        const val LIGHT_GREEN_800 = "#558B2F"
        const val LIGHT_GREEN_900 = "#33691E"
        const val LIGHT_GREEN_A100 = "#CCFF90"
        const val LIGHT_GREEN_A200 = "#B2FF59"
        const val LIGHT_GREEN_A400 = "#76FF03"
        const val LIGHT_GREEN_A700 = "#64DD17"

        // Lime colors
        const val LIME_50 = "#F9FBE7"
        const val LIME_100 = "#F0F4C3"
        const val LIME_200 = "#E6EE9C"
        const val LIME_300 = "#DCE775"
        const val LIME_400 = "#D4E157"
        const val LIME_500 = "#CDDC39"
        const val LIME_600 = "#C0CA33"
        const val LIME_700 = "#AFB42B"
        const val LIME_800 = "#9E9D24"
        const val LIME_900 = "#827717"
        const val LIME_A100 = "#F4FF81"
        const val LIME_A200 = "#EEFF41"
        const val LIME_A400 = "#C6FF00"
        const val LIME_A700 = "#AEEA00"

        // Yellow colors
        const val YELLOW_50 = "#FFFDE7"
        const val YELLOW_100 = "#FFF9C4"
        const val YELLOW_200 = "#FFF59D"
        const val YELLOW_300 = "#FFF176"
        const val YELLOW_400 = "#FFEE58"
        const val YELLOW_500 = "#FFEB3B"
        const val YELLOW_600 = "#FDD835"
        const val YELLOW_700 = "#FBC02D"
        const val YELLOW_800 = "#F9A825"
        const val YELLOW_900 = "#F57F17"
        const val YELLOW_A100 = "#FFFF8D"
        const val YELLOW_A200 = "#FFFF00"
        const val YELLOW_A400 = "#FFEA00"
        const val YELLOW_A700 = "#FFD600"

        // Amber colors
        const val AMBER_50 = "#FFF8E1"
        const val AMBER_100 = "#FFECB3"
        const val AMBER_200 = "#FFE082"
        const val AMBER_300 = "#FFD54F"
        const val AMBER_400 = "#FFCA28"
        const val AMBER_500 = "#FFC107"
        const val AMBER_600 = "#FFB300"
        const val AMBER_700 = "#FFA000"
        const val AMBER_800 = "#FF8F00"
        const val AMBER_900 = "#FF6F00"
        const val AMBER_A100 = "#FFE57F"
        const val AMBER_A200 = "#FFD740"
        const val AMBER_A400 = "#FFC400"
        const val AMBER_A700 = "#FFAB00"

        // Orange colors
        const val ORANGE_50 = "#FFF3E0"
        const val ORANGE_100 = "#FFE0B2"
        const val ORANGE_200 = "#FFCC80"
        const val ORANGE_300 = "#FFB74D"
        const val ORANGE_400 = "#FFA726"
        const val ORANGE_500 = "#FF9800"
        const val ORANGE_600 = "#FB8C00"
        const val ORANGE_700 = "#F57C00"
        const val ORANGE_800 = "#EF6C00"
        const val ORANGE_900 = "#E65100"
        const val ORANGE_A100 = "#FFD180"
        const val ORANGE_A200 = "#FFAB40"
        const val ORANGE_A400 = "#FF9100"
        const val ORANGE_A700 = "#FF6D00"

        // Deep Orange colors
        const val DEEP_ORANGE_50 = "#FBE9E7"
        const val DEEP_ORANGE_100 = "#FFCCBC"
        const val DEEP_ORANGE_200 = "#FFAB91"
        const val DEEP_ORANGE_300 = "#FF8A65"
        const val DEEP_ORANGE_400 = "#FF7043"
        const val DEEP_ORANGE_500 = "#FF5722"
        const val DEEP_ORANGE_600 = "#F4511E"
        const val DEEP_ORANGE_700 = "#E64A19"
        const val DEEP_ORANGE_800 = "#D84315"
        const val DEEP_ORANGE_900 = "#BF360C"
        const val DEEP_ORANGE_A100 = "#FF9E80"
        const val DEEP_ORANGE_A200 = "#FF6E40"
        const val DEEP_ORANGE_A400 = "#FF3D00"
        const val DEEP_ORANGE_A700 = "#DD2C00"

        // Brown colors
        const val BROWN_50 = "#EFEBE9"
        const val BROWN_100 = "#D7CCC8"
        const val BROWN_200 = "#BCAAA4"
        const val BROWN_300 = "#A1887F"
        const val BROWN_400 = "#8D6E63"
        const val BROWN_500 = "#795548"
        const val BROWN_600 = "#6D4C41"
        const val BROWN_700 = "#5D4037"
        const val BROWN_800 = "#4E342E"
        const val BROWN_900 = "#3E2723"

        // Grey colors
        const val GREY_50 = "#FAFAFA"
        const val GREY_100 = "#F5F5F5"
        const val GREY_200 = "#EEEEEE"
        const val GREY_300 = "#E0E0E0"
        const val GREY_400 = "#BDBDBD"
        const val GREY_500 = "#9E9E9E"
        const val GREY_600 = "#757575"
        const val GREY_700 = "#616161"
        const val GREY_800 = "#424242"
        const val GREY_900 = "#212121"

        // Blue Grey colors
        const val BLUE_GREY_50 = "#ECEFF1"
        const val BLUE_GREY_100 = "#CFD8DC"
        const val BLUE_GREY_200 = "#B0BEC5"
        const val BLUE_GREY_300 = "#90A4AE"
        const val BLUE_GREY_400 = "#78909C"
        const val BLUE_GREY_500 = "#607D8B"
        const val BLUE_GREY_600 = "#546E7A"
        const val BLUE_GREY_700 = "#455A64"
        const val BLUE_GREY_800 = "#37474F"
        const val BLUE_GREY_900 = "#263238"

        // B&W color
        const val BLACK = "#000000"
        const val WHITE = "#FFFFFF"
    }

}