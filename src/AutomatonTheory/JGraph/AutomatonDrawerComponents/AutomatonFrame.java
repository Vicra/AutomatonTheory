package AutomatonTheory.JGraph.AutomatonDrawerComponents;

import AutomatonTheory.Logic.DeterministicFiniteAutomaton;
import AutomatonTheory.Logic.State;
import AutomatonTheory.Logic.Transition;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AutomatonFrame extends JInternalFrame {
    public DeterministicFiniteAutomaton dfa = new DeterministicFiniteAutomaton();
    public List<mxCell> Nodes = new ArrayList<mxCell>();
    public List<mxCell> Transitions  = new ArrayList<mxCell>();

    private int circleRadius = 75;

    final mxGraph graph = new mxGraph();
    Object parent = graph.getDefaultParent();

    public AutomatonFrame(){
        super("Automaton Grapher!");

        graph.getModel().beginUpdate();
        graph.getModel().endUpdate();

        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graph.setMultigraph(false);
        graph.setAllowDanglingEdges(false);
        graphComponent.setConnectable(true);
        graphComponent.setToolTips(true);

        // Enables rubberband selection
        new mxRubberband(graphComponent);
        new mxKeyboardHandler(graphComponent);
        getContentPane().add(graphComponent);

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int buttonType = e.getButton();

                if (buttonType == 1) {
                    if (e.getClickCount() == 2) {
                        Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                        if (cell != null) {


                        } else {
                            AddState(e);
                        }
                    }
                }
                if (buttonType == 3) {
                    Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                    if (cell != null) {
                        mxCell node = (mxCell)cell;
                        String style = node.getStyle();

                        if(style.contains("shape=doubleEllipse")){
                            graph.setCellStyles(mxConstants.STYLE_SHAPE,"ellipse",new Object[]{node});
                        }
                        else if(style.contains("shape=ellipse")){
                            graph.setCellStyles(mxConstants.STYLE_SHAPE,"doubleEllipse",new Object[]{node});
                        }

                        graphComponent.refresh();
                        if(toggleAcceptanceState(graph.getLabel(cell))){
                            System.out.println("es aceptado" + graph.getLabel(cell));
                        }
                    }
                }
            }
        });
    }

    private void AddState(MouseEvent e) {
        String nombre = getNameForNewState();
        if(dfa.addState(new State(nombre, false, false))){
            mxCell v1 = (mxCell)graph.insertVertex(parent, null, nombre, e.getX() - 25, e.getY() - 25, circleRadius, circleRadius, "shape=ellipse;perimeter=ellipsePerimeter");
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", new Object[]{v1});
            Nodes.add(v1);
        }

    }
    public boolean AddState() {
        String nombre = getNameForNewState();
        if(dfa.addState(new State(nombre, false, false))){
            mxCell v1 = (mxCell)graph.insertVertex(parent, null, nombre,450,450, circleRadius, circleRadius, "shape=ellipse;perimeter=ellipsePerimeter");
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", new Object[]{v1});
            Nodes.add(v1);
            return true;
        }
        return false;
    }
    public boolean AddTransition(String originStateName, String destinyStateName, String symbol){
        State originState = dfa.get_automaton().getState(originStateName);
        State destinyState = dfa.get_automaton().getState(destinyStateName);
        if(originState.addTransition(new Transition(destinyState, symbol))){
            mxCell nodeOrigin = getNode(originStateName);
            mxCell nodeDestiny = getNode(destinyStateName);
            mxCell trans;

            Boolean hasTwoPaths = false;
            for(mxCell transition : Transitions){
                if(transition.getSource().getValue().toString().equals(nodeOrigin.getValue().toString()) &&
                        transition.getTarget().getValue().toString().equals(nodeDestiny.getValue().toString())){
                    //get transition and change the value shown
                    hasTwoPaths = true;
                    String currentValue = transition.getValue().toString();
                    transition.setValue(currentValue + " / " + symbol);
                    graph.refresh();
                }
            }
            if(!hasTwoPaths){
                if(nodeOrigin.getValue().toString().equals(nodeDestiny.getValue().toString())){
                    trans = (mxCell)(graph.insertEdge(parent, null, symbol, nodeOrigin, nodeDestiny));
                }
                else{
                    trans = (mxCell)(graph.insertEdge(parent, null, symbol, nodeOrigin, nodeDestiny,"edgeStyle=elbowEdgeStyle;elbow=horizontal;orthogonal=0.5;"
                            + "entryX=0.5;entryY=0;entryPerimeter=1;rounded=true;arcsize=12"));
                }
                Transitions.add(trans);
            }
            return true;
        }
        else{
            return false;
        }
    }

    public boolean RemoveState(String stateName){
        if(dfa.removeState(stateName)){
            mxCell node = getNode(stateName);

            //remove transitions
            for(mxCell item : Transitions){
                if(item.getSource().getValue().toString().equals(stateName) ||
                        item.getTarget().getValue().toString().equals(stateName)){
                    graph.removeCells(new Object[]{item});
                    Transitions.remove(item);
                }
            }

            //remove state
            graph.removeCells(new Object[]{node});
            Nodes.remove(node);

            return true;
        }
        return false;
    }

    public boolean SetInitialState(String stateName){
        if(dfa.get_automaton().setInitialState(stateName)){
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7");
            for(mxCell node : Nodes){
                if(node.getValue().toString().equals(stateName)){
                    graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#9FF781", new Object[]{node});
                }
                else{
                    graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#81BEF7", new Object[]{node});
                }
            }
            return true;
        }
        return false;
    }

    public boolean setAcceptanceState(String stateName){
        return dfa.get_automaton().setAcceptanceState(stateName);
    }

    public boolean toggleAcceptanceState(String stateName){
        return dfa.get_automaton().toggleAcceptanceState(stateName);
    }

    public boolean EvaluateAutomaton(String evaluateString){
        return dfa.evaluateString(evaluateString);
    }

    public mxCell getNode(String nodeName){
        for (mxCell item : Nodes){
            if(item.getValue().equals(nodeName)){
                return item;
            }
        }
        return new mxCell();
    }


    public String getNameForNewState(){
        return "q" + Nodes.size();
    }
}