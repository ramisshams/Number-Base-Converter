package converter // Do not delete this line

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.*

var sourceBase = BigDecimal.ZERO
var targetBase = BigDecimal.ZERO
val alphabet = "abcdefghijklmnopqrstuvwxyz"

fun menu() {
    println("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
    var bases = readln().split(" ", limit = 2)
    while (bases[0] != "/exit") {
        sourceBase = bases[0].toBigDecimal()
        targetBase = bases[1].toBigDecimal()
        println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
        var input = readln()
        while (input != "/back") {
            from(to(input))
            println()
            println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
            input = readln()
        }
        println()
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        bases = readln().split(" ", limit = 2)
    }
}

fun from(decimal: BigDecimal) {
    print("Conversion result: ")
    val binaryList = mutableListOf<BigDecimal>()
    var quotient = decimal.toString().split(".")[0].toBigDecimal()
    while (quotient >= BigDecimal.ONE) {
        binaryList.add(0, quotient % targetBase)
        quotient = quotient.divide(targetBase, RoundingMode.FLOOR)
    }
    val hexList = mutableListOf<String>()
    for (i in binaryList) {
        if (i < BigDecimal.TEN) {
            hexList.add(i.toString())
        } else {
            hexList.add(alphabet.uppercase()[i.toInt() - 10].toString())
        }
    }
    var fractional = decimal - decimal.toString().split(".")[0].toBigDecimal()
    val fractionalList = mutableListOf<BigDecimal>()
    while (fractional.toDouble() % 1.0 != 0.0 && fractionalList.size < 5) {
        fractionalList.add((fractional * targetBase).setScale(0, RoundingMode.FLOOR))
        fractional = fractional * targetBase - (fractional * targetBase).setScale(0, RoundingMode.FLOOR)
    }
    val hexFractionalList = mutableListOf<String>()
    for (i in fractionalList) {
        if (i < BigDecimal.TEN) {
            hexFractionalList.add(i.toString())
        } else {
            hexFractionalList.add(alphabet.uppercase()[i.toInt() - 10].toString())
        }
    }
    if (hexList.isEmpty()) hexList.add("0")
    while (hexFractionalList.size < 5) hexFractionalList.add("0")
    if (decimal.toString().split(".").size == 1) {
        println(hexList.joinToString(""))
    } else {
        println(hexList.joinToString("") + "." + hexFractionalList.joinToString(""))
    }
}

fun to(input: String): BigDecimal {
    var integer = ""
    var fractional = ""
    if (input.contains('.')) {
        integer = input.split(".")[0]
        fractional = input.split(".")[1]
    } else {
        integer = input.split(".")[0]
        fractional = "0"
    }
    var targetInteger = BigDecimal.ZERO
    var targetFractional = BigDecimal.ZERO
    for (i in integer.indices) {
        if (integer[i] !in alphabet && integer[i] !in alphabet.uppercase()) {
            targetInteger += integer[i].toString().toBigDecimal() * sourceBase.pow(integer.length - i - 1)
        } else if (integer[i] in alphabet) {
            targetInteger += (alphabet.indexOf(integer[i]) + 10).toBigDecimal() * sourceBase.pow(integer.length - i - 1)
        } else {
            targetInteger += (alphabet.uppercase().indexOf(integer[i]) + 10).toBigDecimal() * sourceBase.pow(integer.length - i - 1)
        }
    }
    for (i in fractional.indices) {
        if (fractional[i] !in alphabet && fractional[i] !in alphabet.uppercase()) {
            targetFractional += fractional[i].toString().toBigDecimal() * (1.0 / sourceBase.toDouble().pow(1 + i)).toBigDecimal()
        } else if (fractional[i] in alphabet) {
            targetFractional += (alphabet.indexOf(fractional[i]) + 10).toBigDecimal() * (1.0 / sourceBase.toDouble().pow(1 + i)).toBigDecimal()
        } else {
            targetFractional += (alphabet.uppercase().indexOf(fractional[i]) + 10).toBigDecimal() * (1.0 / sourceBase.toDouble().pow(1 + i)).toBigDecimal()
        }
    }
    if (input.contains('.')) return targetInteger + targetFractional.setScale(5, RoundingMode.FLOOR) else return targetInteger
}

fun main() {
    menu()
}