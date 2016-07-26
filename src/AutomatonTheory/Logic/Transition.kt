package AutomatonTheory.Logic


open class Transition() {
    open var DestinyState : State = State()
    open var Symbol : String = ""

    constructor(destinyState:State, symbol : String) : this(){
        DestinyState = destinyState
        Symbol = symbol
    }

    constructor(destinyStateName:String, symbol:String) : this(){

    }
}