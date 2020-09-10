package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        final String BASE_PATH = "src/Assets";
        final String EXTENSION = ".mp4";

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("E-Cig Simulation - Team Walking Tacos");

        Button startButton = (Button) root.lookup("#startButton");
        startButton.setText("Start");

        Button buttonR = (Button) root.lookup("#buttonR");
        buttonR.setText("Accept");
        buttonR.setDisable(true);
        buttonR.setOpacity(0);

        Button buttonL = (Button) root.lookup("#buttonL");
        buttonL.setText("Reject");
        buttonL.setDisable(true);
        buttonL.setOpacity(0);

        MediaView mediaView = (MediaView) root.lookup("#mediaView");

        ArrayList<Video> videos = new ArrayList<Video>();

        // Create an InputStreamReader to read in the data file containing video titles.
        InputStreamReader videoIsr = null;
        try {
            videoIsr = new InputStreamReader(this.getClass().getResourceAsStream("/Assets/Videos.csv"));
        } catch(Exception e) {System.exit(1);}

        // Create a Scanner to read in individual data for setting up videos.
        Scanner videoScr = new Scanner(videoIsr);

        // Take the first line of the text file and discard it because it is just a header.
        String videoHeader = videoScr.nextLine();

        // Read from the videos file until it runs out of data.
        while (videoScr.hasNext()) {

            // Create a Scanner to process the individual line of data.
            Scanner videoData = new Scanner(videoScr.nextLine());
            videoData.useDelimiter(",");

            // Extract the necessary data for creating a Video object.
            String title = videoData.next();
            String nextAccTitle = videoData.next();
            String nextRejTitle = videoData.next();

            // Create a new Video object based on the file data and add it to the list of existing videos.
            Video newVideo = new Video(title, nextAccTitle, nextRejTitle);
            videos.add(newVideo);
            videoData.close();
        }
        videoScr.close();

        ArrayList<Video> chosenPath = new ArrayList<Video>();

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setTitle("Information");

        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            startButton.setOpacity(0);

            chosenPath.add(videos.get(0));

            MediaPlayer introPlayer = chosenPath.get(0).makeMediaPlayer(BASE_PATH, EXTENSION);
            mediaView.setMediaPlayer(introPlayer);

            introPlayer.setOnEndOfMedia(() -> {
                buttonR.setDisable(false);
                buttonR.setOpacity(1);
                buttonL.setDisable(false);
                buttonL.setOpacity(1);

                introPlayer.dispose();
            });
        });

        buttonR.setOnAction(event -> {
            buttonL.setDisable(true);
            buttonL.setOpacity(0);
            buttonR.setDisable(true);
            buttonR.setOpacity(0);

            popup.setHeaderText("The FDA says: ");
            popup.setContentText("E-cigarettes contain dangerous chemicals as well as metal particles that can cause" +
                    " serious lung damage. It's never too early to take care of your health. Find more info at " +
                    "https://www.fda.gov/tobacco-products/public-health-education/think-e-cigs-cant-harm-teens-health");
            popup.showAndWait();

            Video toPlay = null;
            for (Video vid : videos) {
                String nextVidTitle = chosenPath.get(chosenPath.size() - 1).getNextAccTitle();
                if (vid.getTitle().equals(nextVidTitle)) {
                    toPlay = vid;
                }
            }

            chosenPath.add(toPlay);
            MediaPlayer mediaPlayer = toPlay.makeMediaPlayer(BASE_PATH, EXTENSION);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnEndOfMedia(() -> {
                buttonR.setDisable(false);
                buttonR.setOpacity(1);
                buttonL.setDisable(false);
                buttonL.setOpacity(1);

                mediaPlayer.dispose();
            });
        });

        buttonL.setOnAction(event -> {
            buttonL.setDisable(true);
            buttonL.setOpacity(0);
            buttonR.setDisable(true);
            buttonR.setOpacity(0);

            popup.setHeaderText("Harvard Medical School says: ");
            popup.setContentText("The nicotine in e-cigarettes is highly addictive and can affect the development " +
                    "of the brain, especially in teens and young adults. By refusing Vinny's offer you've taken your " +
                    "first step in protecting your health. Find more information at " +
                    "https://www.health.harvard.edu/blog/can-vaping-damage-your-lungs-what-we-do-and-dont-know-2019090417734");
            popup.showAndWait();

            Video toPlay = null;
            for (Video vid : videos) {
                String nextVidTitle = chosenPath.get(chosenPath.size() - 1).getNextRejTitle();
                if (vid.getTitle().equals(nextVidTitle)) {
                    toPlay = vid;
                }
            }

            chosenPath.add(toPlay);
            MediaPlayer mediaPlayer = toPlay.makeMediaPlayer(BASE_PATH, EXTENSION);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnEndOfMedia(() -> {
                buttonR.setDisable(false);
                buttonR.setOpacity(1);
                buttonL.setDisable(false);
                buttonL.setOpacity(1);

                mediaPlayer.dispose();
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
