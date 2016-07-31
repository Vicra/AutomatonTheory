package AutomatonTheory.Kotlin.AutomatonLogic

open class Transition {
    open var DestinyState = State()
    open var Symbol = ""

    constructor() {
    }

    constructor(destinyState: State, symbol: String) {
        DestinyState = destinyState
        Symbol = symbol
    }
}
