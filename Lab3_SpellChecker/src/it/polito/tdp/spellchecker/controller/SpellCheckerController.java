package it.polito.tdp.spellchecker.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.spellchecker.model.Dictionary;
import it.polito.tdp.spellchecker.model.RichWord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SpellCheckerController {

	private Dictionary model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> cmbBox;

    @FXML
    private TextArea txtInserimento;

    @FXML
    private Button btnSpell;

    @FXML
    private TextArea txtResult;

    @FXML
    private Label labelError;

    @FXML
    private Button btnClear;

    @FXML
    private Label labelTime;

    @FXML
    void doClearText(ActionEvent event) {

    	txtResult.clear();
    	txtInserimento.clear();
    	labelError.setText("Number of errors");
    	labelTime.setText("Spell check time");
    }

    @FXML
    void doSpellCheck(ActionEvent event) {

    	long t1 = System.nanoTime();
    	String lingua = cmbBox.getValue();
    	if(lingua.equals("English"))
    		lingua = "rsc/English.txt"; 
    	else
    		lingua = "rsc/Italian.txt"; 
    	String result = txtInserimento.getText();
    	
    	result = result.toLowerCase();
    	result = result.replaceAll("[\\p{Punct}]", "");
    	//txtResult.appendText(result);
    	String[] parole = result.split(" ");
    	LinkedList<String> listaParole = new LinkedList<String>(Arrays.asList(parole));
    	model.loadDictionary(lingua);
    	List<RichWord> listaOutput = model.spellCheckText(listaParole);
    	int numeroSbagliate = 0;
    	for(RichWord rw : listaOutput){
    		if(!rw.isCorretta()){
    			txtResult.appendText(rw.getParola()+"\n");
    			numeroSbagliate++;
    		}
    	}
    	long t2 = System.nanoTime();
    	
    	labelTime.setText("Spell check completed in "+(t2-t1)/1e9+" seconds");
    	if(numeroSbagliate==1)
    		labelError.setText("The text contains "+numeroSbagliate+"error");
    	if(numeroSbagliate!=1)
    		labelError.setText("The text contains "+numeroSbagliate+"errors");

    }

    @FXML
    void initialize() {
        assert cmbBox != null : "fx:id=\"cmbBox\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert txtInserimento != null : "fx:id=\"txtInserimento\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert btnSpell != null : "fx:id=\"btnSpell\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert labelError != null : "fx:id=\"labelError\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert labelTime != null : "fx:id=\"labelTime\" was not injected: check your FXML file 'SpellChecker.fxml'.";

        cmbBox.getItems().addAll("English", "Italian");
        if(cmbBox.getItems().size()>0)
        	cmbBox.setValue(cmbBox.getItems().get(0));
    }

	public void setModel(Dictionary model) {
		this.model = model;		
	}
}
