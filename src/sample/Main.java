package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.awt.*;
import java.io.Console;

public class Main extends Application {

    public VBox answerArea, chatArea;       // beide Bereiche als globale Variablen
    double chatVerlaufHöhe = 0;
    enum Richtung { LINKS, RECHTS };

    @Override
    public void start(Stage stage) throws Exception{
        BorderPane window = new BorderPane();                   // das ganze Fenster
        chatArea = new VBox();                                  // Chat Area
        chatArea.setPadding(new Insets(10));  // Innenabstände zu den Rändern
        chatArea.setSpacing(10);                                // Abstand der Elemente zueinander in der vbox
        chatArea.setStyle("-fx-background-color: #E5DDD5;");    // Hintergrundfarbe gräulich
        chatArea.setMinHeight(700);
        window.setCenter(chatArea);                             // Mitte in BorderPane (left, right, top fehlen)


        answerArea = new VBox();                                // AnswerArea
        answerArea.setPadding(new Insets(5)); // Innenabstände zu den Rändern
        answerArea.setSpacing(5);                               // Abstand der Elemente zueinander in der vbox
        answerArea.setStyle("-fx-background-color: #FFFFFF;");  // Hintergrund weiß
        answerArea.setAlignment(Pos.CENTER);                    // Buttons zentrieren
        window.setBottom(answerArea);                           // Unten in BorderPane


        Scene scene = new Scene(window, 640, 800); // Maße des Fensters 640x800 Pixel
        stage.setTitle("Componentware Projekt");
        stage.setResizable(false);                              // Größenänderung verhindern
        stage.centerOnScreen();                                 // Fenster auf Bildschirm zentrieren
        stage.setScene(scene);
        stage.show();

        // ein (\n) im Text bewirkt einen expliziten Zeilenumbruch
        String text = "Du stehst auf einem großen Feld. Vor dir in der Ferne siehst du ein großes Schloß auf einem kleinen Hügel. " +
                "Ein gruseliges Geräuch kommt dir aus Richtung des Schlosses entgegen. \nEs hört sich nach einem Schrei an.";

        adventureStep(Richtung.LINKS, text);             // Aufruf eines Schrittes links
        String frage = "Was machst du?";
        adventureStep(Richtung.LINKS, frage);            // Aufruf eines Schrittes rechts

        String[] antworten = { "Ich renne so schnell es geht weg", "Ich nähere mich vorsichtig dem Schloss", "Ich bleibe angestarrt stehen"};
        answerArea.getChildren().clear();           // vorige Elemente löschen
        // die Antworten in der answerArea anzeigen
        for (String s : antworten) {
            Button b = new Button(s);                   // je Antwort einen Buttom machen mit dem Text drin
            b.setOnAction(e -> antwortGewählt(s));      // verbinde den Click-Event mit der Funktion "antwortGewählt", als Parameter den Text
            answerArea.getChildren().add(b);            // Füge den Button in die answerArea ein
        }
    }

    private void antwortGewählt(String s) {         // wird beim Auswählen einer Antwort aufgerufen
        adventureStep(Richtung.RECHTS, s);          // gibt die Antwort rechts in der chatArea aus
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void adventureStep(Richtung richtung, String text) {
        // Eingabeparameter: richtung:  Soll Chat links oder rechts angezeigt werden? Auch die Farbe ändert sich.
        //                   text:      Anzuzeigender Text

        Text t = new Text (text);                           // Text-Control
        t.setFont(Font.font ("Verdana", 14));   // Setze Font und Größe
        if (t.getLayoutBounds().getWidth() > 500)           // nur wenn Breite des Textes über 500 Pixel dann Wrapping
            t.setWrappingWidth(500);                        // Width setzen. Dadurch bricht der Text automatisch um.

        HBox hbox = new HBox();                 // zur Aufnahme eines einzelnen Chats

        // Rechteck in der Größe des Texts + 20 in Höhe und Breite
        Rectangle rec = new Rectangle(t.getLayoutBounds().getWidth() + 20, t.getLayoutBounds().getHeight() + 20);
        if (richtung == Richtung.RECHTS) {                           // wenn als Parameter rechtsbündig gewünscht,
            hbox.setAlignment(Pos.BASELINE_RIGHT);                   // HBox rechtsbündig
            rec.setFill(Color.rgb(220, 248, 198));  // Hellgrün
        } else {
            rec.setFill(Color.rgb(255, 255, 255));  // Weiß bei linksbündig
        }
        rec.setArcWidth(30);                    // Ecken-Abrundungen
        rec.setArcHeight(30);
        rec.setStroke(Color.LIGHTGREY);         // Randfarbe Grau
        rec.setStrokeWidth(1);                  // Randdicke 1

        StackPane stack = new StackPane(rec, t);  // StackPane, damit mehrere Elemente zentriert übereinander
        hbox.getChildren().add(stack);          // Aufnahme der StackPane in HBox
        chatArea.getChildren().add(hbox);       // Aufnahme HBox in ChatArea

        // Gesamthöhe aller Chats ermitteln. Dies funktioniert nicht zuverlässig durch Addition der Höhe aller
        // Einzelchats. Daher als globale Veriable mitführen.
        chatVerlaufHöhe += rec.getLayoutBounds().getHeight() + 10;   // Erhöhen um Höhe des Rechtecks + 10 Abstand
        while (chatVerlaufHöhe > 700) {                              // Solange höher als 700 Pixel (chatArea-Höhe)
            Node n = chatArea.getChildren().get(0);                  // Hole ersten Chat
            chatVerlaufHöhe = chatVerlaufHöhe - n.getLayoutBounds().getHeight() - 10;   // Subtrahiere dessen Höhe
            chatArea.getChildren().remove(0);                     // und entferne ihn
        }
    }
}

