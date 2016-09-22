package AutomatonsTheory.TabDemo

import AutomatonsTheory.AutomatonExtensions.*
import AutomatonsTheory.AutomatonLogic.Automaton
import AutomatonsTheory.AutomatonLogic.Automatons
import AutomatonsTheory.MainWindow
import AutomatonsTheory.Swing.AutomatonDrawerComponents.AutomatonFrame
import AutomatonsTheory.Swing.DialogBoxes.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.swing.*


class TabComponentsDemo(title: String) : JFrame(title), ActionListener {

    private val pane = JTabbedPane()
    internal var field = JTextField()

    internal val evaluateAutomatonButton = JButton("Evaluate")

    init {
        field.setBounds(20, 60, 150, 40)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        initMenu()
        add(pane)

        evaluateAutomatonButton.isVisible = true
        evaluateAutomatonButton.actionCommand = "evaluate"
        evaluateAutomatonButton.addActionListener(this)
    }

    override fun actionPerformed(e: ActionEvent){
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)

        if (e.actionCommand == "evaluate"){
            val evalutaCadena = field.text
            if (iframe.EvaluateAutomaton(evalutaCadena)) {
                JOptionPane.showMessageDialog(iframe, "Cadena paso")
            } else {
                JOptionPane.showMessageDialog(iframe, "La Cadena no paso", "Error", JOptionPane.ERROR_MESSAGE)
            }
        }
    }

    fun runTest() {
        pane.removeAll()
        isVisible = true
    }

    fun newTab(name:String, type:String, alphabet:MutableList<String>){
        var newFrame: AutomatonFrame = AutomatonFrame(DeterministicFiniteAutomaton(name))
        if(type == "DFA"){
            newFrame = AutomatonFrame(DeterministicFiniteAutomaton(name, alphabet))
        }
        if(type == "NFA"){
            newFrame = AutomatonFrame(NonDeterministicFiniteAutomaton(name, alphabet))
        }
        if(type == "NFAe") {
            newFrame = AutomatonFrame(NonDeterministicFiniteEpsilonAutomaton(name, alphabet))
        }
        if(type == "PDA") {
            newFrame = AutomatonFrame(PushDownAutomaton(name, alphabet))
        }
        if(type == "TuringMachine") {
            newFrame = AutomatonFrame(TuringMachine(name, alphabet))
        }
        newFrame.setVisible(true)

        pane.add(name + "(Type: " + type + ")", newFrame)
        initTabComponent(pane.tabCount - 1)
        pane.selectedIndex = pane.tabCount -1
    }

    fun newTabOpenAutomaton(loadedAutomaton:Automaton){
        var newFrame: AutomatonFrame = AutomatonFrame(loadedAutomaton)
        newFrame.setVisible(true)
        pane.add(loadedAutomaton.AutomatonName + "(Type: " + loadedAutomaton.Type.toString() + ")", newFrame)
        initTabComponent(pane.tabCount - 1)
        pane.selectedIndex = pane.tabCount -1
        drawLoadedAutomaton(loadedAutomaton,newFrame)
    }

    fun createAutomaton(){
        val dialog = CreateAutomatonDialog()
        dialog.displayGUI()

        if(dialog.valor == 0){
            newTab(dialog.automatonNameTextField.text, dialog.automatonType, dialog.charactersList)
        }
    }

    fun drawLoadedAutomaton(automaton:Automaton, frame : AutomatonFrame) : Unit {
        frame.RemoveCellsFromGraph()
        MainWindow.redrawFrame(automaton, frame)
    }

    fun openAutomaton(parent: JFrame){
        var filename:String
        var dir:String
        val openFileChooser = JFileChooser()
        val rVal = openFileChooser.showOpenDialog(parent)
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = openFileChooser.getSelectedFile().getName()
            dir = openFileChooser.getCurrentDirectory().toString()

            val fileIn = FileInputStream(dir + "/" +filename)
            val input = ObjectInputStream(fileIn)
            val loadedAutomaton: Automaton = input.readObject() as Automaton
            input.close()
            fileIn.close()
            newTabOpenAutomaton(loadedAutomaton)
        }
    }

    fun saveAutomaton(parent: JFrame){
        var filename:String
        var dir:String
        val saveFileChooser = JFileChooser()
        // Demonstrate "Open" dialog:
        val rVal = saveFileChooser.showSaveDialog(parent)
        if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = saveFileChooser.getSelectedFile().getName()
            dir = saveFileChooser.getCurrentDirectory().toString()

            val fileOut = FileOutputStream(dir + "/" +filename + ".ser")
            val out = ObjectOutputStream(fileOut)
            val toSaveAutomaton = (pane.getComponentAt(pane.selectedIndex) as AutomatonFrame).automaton
            out.writeObject(toSaveAutomaton)
            out.close()
            fileOut.close()
        }
    }

    private fun initTabComponent(i: Int) {
        pane.setTabComponentAt(i,ButtonTabComponent(pane))
    }

    //Setting menu

    private fun initMenu() {
        val menuBar = JMenuBar()
        //menu ITEMS
        val createAutomatonItem = JMenuItem("Create Automaton")
        val openAutomatonItem = JMenuItem("Open Automaton")
        val saveAutomatonItem = JMenuItem("Save Automaton")
        val resetItem = JMenuItem("Reset JTabbedPane")

        val addStateItem = JMenuItem("Add State")
        val removeStateItem = JMenuItem("Remove State")

        val addTransitionItem = JMenuItem("Add Transition")
        val removeTransitionItem = JMenuItem("Remove Transition")

        val setInitialStateItem = JMenuItem("Set Initial State")
        val convertToRegexItem = JMenuItem("Convert to REGEX")
        val convertToDFAItem = JMenuItem("Convert to DFA")
        val minimizeItem = JMenuItem("Minimize")

        val unionItem = JMenuItem("Union")
        val interseccionItem = JMenuItem("Interseccion")
        val complementoItem = JMenuItem("Complemento")
        val restaItem = JMenuItem("Resta")

        //accelerators
        createAutomatonItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)
        openAutomatonItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)
        saveAutomatonItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)
        addStateItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK)
        addTransitionItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK)
        setInitialStateItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK)
        convertToRegexItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK)
        unionItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK)
        complementoItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK)

        //action listeners
        createAutomatonItem.addActionListener { createAutomaton() }
        openAutomatonItem.addActionListener { openAutomaton(this) }
        saveAutomatonItem.addActionListener { saveAutomaton(this)}
        addStateItem.addActionListener { addState()}
        removeStateItem.addActionListener { removeState()}
        addTransitionItem.addActionListener { addTransition()}
        removeTransitionItem.addActionListener { removeTransition()}
        setInitialStateItem.addActionListener { setInitialState()}

        convertToRegexItem.addActionListener { convertToRegex()}
        convertToDFAItem.addActionListener { convertToDFA()}
        minimizeItem.addActionListener { minimize()}

        unionItem.addActionListener { union()}
        interseccionItem.addActionListener { interseccion()}
        complementoItem.addActionListener { complemento()}
        restaItem.addActionListener { resta()}

        // add items
        val optionsMenu = JMenu("Automaton")
        optionsMenu.add(createAutomatonItem)
        optionsMenu.add(openAutomatonItem)
        optionsMenu.add(saveAutomatonItem)

        val moreOptionsMenu = JMenu("Options")
        moreOptionsMenu.add(addStateItem)
        moreOptionsMenu.add(removeStateItem)
        moreOptionsMenu.addSeparator()
        moreOptionsMenu.add(addTransitionItem)
        moreOptionsMenu.add(removeTransitionItem)
        moreOptionsMenu.addSeparator()
        moreOptionsMenu.add(setInitialStateItem)

        val convertsMenu = JMenu("Convert")
        convertsMenu.add(convertToRegexItem)
        convertsMenu.add(convertToDFAItem)
        convertsMenu.add(minimizeItem)

        val combineMenu = JMenu("Combine")
        combineMenu.add(unionItem)
        combineMenu.add(interseccionItem)
        combineMenu.add(complementoItem)
        combineMenu.add(restaItem)

        menuBar.add(optionsMenu)
        menuBar.add(moreOptionsMenu)
        menuBar.add(convertsMenu)
        menuBar.add(combineMenu)
        menuBar.add(this.field)
        menuBar.add(evaluateAutomatonButton)

        jMenuBar = menuBar
    }

    private fun resta() {
        var i = 0
        var automatonList:MutableList<String> = mutableListOf()

        //mostrar todos las tabs abiertas en el dialog
        while( i < pane.tabCount){
            automatonList.add((pane.getComponentAt(i) as AutomatonFrame).automaton.AutomatonName)
            i++
        }
        val dialog = CombineAutomatonsDialog(automatonList,"Resta")
        dialog.displayGUI()
        if (dialog.valor == 0) {
            var automatonAName = dialog.automatonA
            var automatonBName = dialog.automatonB

            var automatonA:DeterministicFiniteAutomaton = getTabAutomaton(automatonAName)
            var automatonB:DeterministicFiniteAutomaton = getTabAutomaton(automatonBName)

            var automatonCombinations:AutomatonCombinations = AutomatonCombinations()
            var unionAutomaton:DeterministicFiniteAutomaton = automatonCombinations.Resta(automatonA, automatonB)
            newTabOpenAutomaton(unionAutomaton)
        }
    }

    private fun complemento() {
        var i = 0
        var automatonList:MutableList<String> = mutableListOf()

        //mostrar todos las tabs abiertas en el dialog
        while( i < pane.tabCount){
            automatonList.add((pane.getComponentAt(i) as AutomatonFrame).automaton.AutomatonName)
            i++
        }
        val dialog = ComplementAutomatonDialog(automatonList)
        dialog.displayGUI()
        if (dialog.valor == 0) {
            var automatonAName = dialog.automatonA

            var automatonA:DeterministicFiniteAutomaton = getTabAutomaton(automatonAName)

            var automatonCombinations:AutomatonCombinations = AutomatonCombinations()
            var unionAutomaton:DeterministicFiniteAutomaton = automatonCombinations.Complemento(automatonA)
            newTabOpenAutomaton(unionAutomaton)
        }
    }

    private fun interseccion() {
        var i = 0
        var automatonList:MutableList<String> = mutableListOf()

        //mostrar todos las tabs abiertas en el dialog
        while( i < pane.tabCount){
            automatonList.add((pane.getComponentAt(i) as AutomatonFrame).automaton.AutomatonName)
            i++
        }
        val dialog = CombineAutomatonsDialog(automatonList,"Intersection")
        dialog.displayGUI()
        if (dialog.valor == 0) {
            var automatonAName = dialog.automatonA
            var automatonBName = dialog.automatonB

            var automatonA:DeterministicFiniteAutomaton = getTabAutomaton(automatonAName)
            var automatonB:DeterministicFiniteAutomaton = getTabAutomaton(automatonBName)

            var automatonCombinations:AutomatonCombinations = AutomatonCombinations()
            var unionAutomaton:DeterministicFiniteAutomaton = automatonCombinations.Intersection(automatonA, automatonB)
            newTabOpenAutomaton(unionAutomaton)
        }
    }

    private fun union() {
        var i = 0
        var automatonList:MutableList<String> = mutableListOf()

        //mostrar todos las tabs abiertas en el dialog
        while( i < pane.tabCount){
            automatonList.add((pane.getComponentAt(i) as AutomatonFrame).automaton.AutomatonName)
            i++
        }
        val dialog = CombineAutomatonsDialog(automatonList,"Union")
        dialog.displayGUI()
        if (dialog.valor == 0) {
            var automatonAName = dialog.automatonA
            var automatonBName = dialog.automatonB

            var automatonA:DeterministicFiniteAutomaton = getTabAutomaton(automatonAName)
            var automatonB:DeterministicFiniteAutomaton = getTabAutomaton(automatonBName)

            var automatonCombinations:AutomatonCombinations = AutomatonCombinations()
            var unionAutomaton:DeterministicFiniteAutomaton = automatonCombinations.Union(automatonA, automatonB) // union
            newTabOpenAutomaton(unionAutomaton)
        }
    }

    private fun getTabAutomaton(automatonName:String):DeterministicFiniteAutomaton{
        var i =0
        while( i < pane.tabCount){
            var currentAutomaton:Automaton =(pane.getComponentAt(i) as AutomatonFrame).automaton
            if(currentAutomaton.AutomatonName.equals(automatonName)){
                return currentAutomaton as DeterministicFiniteAutomaton
            }
            i++
        }
        return DeterministicFiniteAutomaton("")
    }

    private fun evaluate() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        val evalutaCadena = field.text
        if (iframe.EvaluateAutomaton(evalutaCadena)) {
            JOptionPane.showMessageDialog(iframe, "Cadena paso")
        } else {
            JOptionPane.showMessageDialog(iframe, "La Cadena no paso", "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun minimize() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        var dfaAutomaton = iframe.automaton as DeterministicFiniteAutomaton
        newTabOpenAutomaton(dfaAutomaton.minimize())
    }

    private fun convertToDFA() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        var dfa: DeterministicFiniteAutomaton
        if(iframe.automaton.Type == Automatons.NFA){
            dfa = (iframe.automaton as NonDeterministicFiniteAutomaton).toDeterministicFiniteAutomaton()
            newTabOpenAutomaton(dfa)
        }
        else if(iframe.automaton.Type  == Automatons.NFAe){
            dfa = (iframe.automaton as NonDeterministicFiniteEpsilonAutomaton).toNFA().toDeterministicFiniteAutomaton()
            newTabOpenAutomaton(dfa)
        }
    }

    private fun convertToRegex() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        var dfaAutomaton = iframe.automaton as DeterministicFiniteAutomaton
        var regularExpression:String = dfaAutomaton.toRegularExpression()
        JOptionPane.showMessageDialog(iframe, regularExpression)
    }

    private fun setInitialState() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        val dialog = SetInitialStateDialog(iframe.automaton.getStatesNames())
        dialog.displayGUI()
        val state = dialog.newInitialState

        if (dialog.valor == 0) {
            if (iframe.SetInitialState(state)) {

            }
        }
    }

    private fun removeTransition() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        val dialog = RemoveTransitionDialog(iframe.automaton.getStatesNames(),
                iframe.automaton.getAllAlphabet())
        dialog.displayGUI()
        if (dialog.valor == 0) {
            if(iframe.RemoveTransition(dialog.originState, dialog.destinyState, dialog.symbol)){

            }
            else {
                JOptionPane.showMessageDialog(this, "Transition not removed")
            }
        }
    }

    private fun addTransition() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        if(iframe.automaton.Type == Automatons.PDA || iframe.automaton.Type == Automatons.TuringMachine){
            val dialog = AddPushDownTransitionDialog(iframe.automaton.getStatesNames())
            dialog.displayGUI()
            if (dialog.valor == 0) {
                if (iframe.AddTransition(dialog.originState, dialog.destinyState, dialog.symbol)) {
                } else {
                    JOptionPane.showMessageDialog(this, "No se agrego la transicion")
                }
            }
        }
        else{
            val dialog = AddTransitionDialog(iframe.automaton.getStatesNames(),
                    iframe.automaton.getAllAlphabet())
            dialog.displayGUI()
            if (dialog.valor == 0) {
                if (iframe.AddTransition(dialog.originState, dialog.destinyState, dialog.symbol)) {
                } else {
                    JOptionPane.showMessageDialog(this, "No se agrego la transicion")
                }
            }
        }
    }

    private fun removeState() {
        var iframe = (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame)
        val dialog = RemoveStateDialog(iframe.automaton.getStatesNames())
        dialog.displayGUI()
        if (dialog.valor == 0) {
            val stateName = dialog.stateToRemove

            if (iframe.RemoveState(stateName)) {

            }
        }
    }

    private fun addState() {
        (pane.getComponentAt(pane.selectedIndex)as AutomatonFrame).AddState()
    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            //Schedule a job for the event dispatch thread:
            //creating and showing this application's GUI.
            SwingUtilities.invokeLater {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", java.lang.Boolean.FALSE)
                val frame = TabComponentsDemo("TabComponentsDemo")
                frame.extendedState = JFrame.MAXIMIZED_BOTH
                frame.pack()
                frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
                frame.setVisible(true)
                frame.runTest()

            }
        }
    }
}