package org.opental.pdf.restserver;

import java.util.List;
import java.util.TreeSet;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.opental.core.CommandAutoComplete;
import org.opental.core.GuiManager;
import org.opental.core.KeywordEntry;
import org.opental.core.KeywordFinder;
import org.opental.core.model.Command;
import org.opental.core.model.CommandResult;
import org.opental.core.model.CommandType;
import org.opental.core.sut.SutStatus;
import org.opental.plugin.AbstractTestAdaptationPlugin;
import org.opental.plugin.TestAdaptationType;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PdfTestAdaptionPlugin extends AbstractTestAdaptationPlugin {

	/**
	 * unique id of the test adapter
	 */
	public static final String ID = "pdf";
	
	/**
	 * tableview with commands
	 */
	protected static final TableView<CommandResult> commandList = new TableView<>();
	
	/**
	 * results after execution
	 */
	protected static final ObservableList<CommandResult> clData = FXCollections.observableArrayList();		
	
	public PdfTestAdaptionPlugin() {
		super(TestAdaptationType.ADAPTER);
	}

	public String getName() {
		return "PDF Plugin";
	}

	@Override
	public Tab startup() {
		Tab pdfTab = createTab(ID, commandList, clData);
		
		// Add (Test generation)
		Text txtGeneration = new Text();
		txtGeneration.setText("test generation");
		txtGeneration.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
		
		List<KeywordEntry> allModuleKeywords = KeywordFinder.getinstance().getKeywordsByModule(PdfTestAdaptionPlugin.ID);
		TreeSet<String> sortedKeywords = new TreeSet<>();
		for (KeywordEntry aKeyword : allModuleKeywords) {
			sortedKeywords.add(aKeyword.getCommand());
		}
		final CommandAutoComplete addCommand = new CommandAutoComplete(sortedKeywords);
        addCommand.setPromptText("Command");
        addCommand.setMaxWidth(200);
        final TextField addTarget = new TextField();
        addTarget.setMaxWidth(400);
        addTarget.setPromptText("Target");
        final TextField addValue = new TextField();
        addValue.setMaxWidth(400);
        addValue.setPromptText("Value");
 
        final Button addButton = new Button("Go");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
            	Command newCmd = new Command(addCommand.getText(), addTarget.getText(), addValue.getText());
            	
                addCommand.clear();
                addTarget.clear();
                addTarget.setPromptText("");
                addValue.clear();
                addValue.setPromptText("");
                
                PdfResource aResource = new PdfResource();
                GuiManager.getinstance().setTabStatus(PdfTestAdaptionPlugin.ID, SutStatus.CONNECTED);
                aResource.execute(newCmd);
                GuiManager.getinstance().setTabStatus(PdfTestAdaptionPlugin.ID, SutStatus.DISCONNECTED);
            }
        });

//        final Button dbgButton = new Button("Debug");
//        dbgButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//            }
//        });

        
		addCommand.focusedProperty().addListener((arg0, oldValue, newValue) -> {
			if (!newValue && !addCommand.getText().isEmpty()) {
				if (!KeywordFinder.getinstance().isKeywordExisting(getAdaptionID(), addCommand.getText())) {
					addCommand.setBackground(
							new Background(new BackgroundFill(Color.ORANGERED, CornerRadii.EMPTY, Insets.EMPTY)));
					addButton.setDisable(true);
					addTarget.setDisable(false);
					addValue.setDisable(false);
				} else {
					addCommand.setBackground(
							new Background(new BackgroundFill(Color.GREENYELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
					addButton.setDisable(false);
					
					addTarget.setDisable(!KeywordFinder.getinstance().isTargetEnabled(getAdaptionID(), addCommand.getText()));
					addValue.setDisable(!KeywordFinder.getinstance().isValueEnabled(getAdaptionID(), addCommand.getText()));

				    addTarget.setPromptText(KeywordFinder.getinstance().getTargetHint(getAdaptionID(), addCommand.getText()));
				    addValue.setPromptText(KeywordFinder.getinstance().getValueHint(getAdaptionID(), addCommand.getText()));
				}
			}

		});
		
        HBox hbox = new HBox();
        
        hbox.getChildren().addAll(txtGeneration, addCommand, addTarget, addValue, addButton);
        hbox.setSpacing(5);
        hbox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(3))));
        
        BorderPane pane = (BorderPane)pdfTab.getContent();
		pane.setTop(hbox);        

		GuiManager.getinstance().writeLog("Successfully started: " + getName());
		
		return pdfTab;
	}

	public static void addCommandToList(Command aCmd, CommandType aType) {
		String aValue = aCmd.getValue();
		CommandResult tCR = new CommandResult(aCmd.getCommand(), aCmd.getTarget(), aValue, aType);
		Platform.runLater(() -> clData.add(tCR));
	}

	public static void setLatestResult(boolean success, final String aResult) {
		Platform.runLater(() -> clData.get(clData.size() - 1).setLogInfo(aResult));
		Platform.runLater(() -> clData.get(clData.size() - 1).setResult(success));

		Platform.runLater(commandList::refresh);
		Platform.runLater(() -> commandList.scrollTo(clData.size() - 1));
	}

	@Override
	public boolean shutdown() {
		return true;
	}
	
	@Override
	public Class<?> getImplementation() {
		return null;
	}
	
	@Override
	public String getAdaptionID() {
		return ID;
	}

	@Override
	public CombinedConfiguration loadConfig() {
		return loadConfig(ID);
	}
}
