package AutomatonsTheory.AutomatonLogic

import java.io.Serializable

open class Transition : Serializable {
    open var DestinyState = State()
    open var Symbol = ""

    constructor() {
    }

    constructor(destinyState: State, symbol: String) {
        DestinyState = destinyState
        Symbol = symbol
    }
}
