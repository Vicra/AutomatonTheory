package AutomatonsTheory.Swing.FileChooser

import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class FileChooser : JFrame() {
    private val filename = JTextField()
    private val dir = JTextField()

    private val open = JButton("Open")
    private val save = JButton("Save")

    init {
        var p = JPanel()
        open.addActionListener(OpenL())
        p.add(open)
        save.addActionListener(SaveL())
        p.add(save)
        val cp = getContentPane()
        cp.add(p, BorderLayout.SOUTH)
        dir.setEditable(false)
        filename.setEditable(false)
        p = JPanel()
        p.setLayout(GridLayout(2, 1))
        p.add(filename)
        p.add(dir)
        cp.add(p, BorderLayout.NORTH)
    }

    internal inner class OpenL : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val c = JFileChooser()
            // Demonstrate "Open" dialog:
            val rVal = c.showOpenDialog(this@FileChooser)
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName())
                dir.setText(c.getCurrentDirectory().toString())
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                filename.setText("You pressed cancel")
                dir.setText("")
            }
        }
    }

    internal inner class SaveL : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            val c = JFileChooser()
            // Demonstrate "Save" dialog:
            val rVal = c.showSaveDialog(this@FileChooser)
            if (rVal == JFileChooser.APPROVE_OPTION) {
                filename.setText(c.getSelectedFile().getName())
                dir.setText(c.getCurrentDirectory().toString())
            }
            if (rVal == JFileChooser.CANCEL_OPTION) {
                filename.setText("You pressed cancel")
                dir.setText("")
            }
        }
    }

    companion object {

        @JvmStatic fun main(args: Array<String>) {
            run(FileChooser(), 250, 110)
        }

        fun run(frame: JFrame, width: Int, height: Int) {
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE)
            frame.setSize(width, height)
            frame.setVisible(true)
        }
    }
}