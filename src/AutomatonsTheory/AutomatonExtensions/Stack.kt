package AutomatonsTheory.AutomatonExtensions

open class Stack {
    var list:MutableList<String> = mutableListOf()

    fun push(element:String){
        list.add(element)
    }

    fun pop(){
        list.removeAt(list.size - 1)
    }

    fun peek():String{
        return list.get(list.size - 1)
    }
}