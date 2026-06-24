package com.nutriquest.ui;

import com.nutriquest.App;
import com.nutriquest.model.FoodType;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class FoodExplorerScreen extends BorderPane {
    private final App controller;

    private TextField searchField;
    private Button searchBtn;
    private ProgressIndicator progressIndicator;
    private VBox resultCard;

    private Label resultTitle;
    private Label badgeLabel;
    private ImageView resultImageView;
    private Label resultDesc;

    private static final Map<String, FoodType> LOCAL_CLASSIFIER = new HashMap<>();

    static {
        // Pre-defined database mapping
        LOCAL_CLASSIFIER.put("apple", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("banana", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("broccoli", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("carrot", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("spinach", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("milk", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("egg", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("salmon", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("avocado", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("blueberry", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("strawberry", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("orange", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("oatmeal", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("almond", FoodType.GOOD_FOOD);
        LOCAL_CLASSIFIER.put("yogurt", FoodType.GOOD_FOOD);

        LOCAL_CLASSIFIER.put("burger", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("hamburger", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("pizza", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("donut", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("soda", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("cola", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("chips", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("candy", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("chocolate", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("ice cream", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("cake", FoodType.BAD_FOOD);
        LOCAL_CLASSIFIER.put("french fries", FoodType.BAD_FOOD);
    }

    public FoodExplorerScreen(App controller) {
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(25));

        // Header
        Label titleLabel = new Label("Food Explorer");
        titleLabel.getStyleClass().add("text-title");
        titleLabel.setStyle("-fx-font-size: 36px;");

        Label subtitleLabel = new Label("Search any food to discover its nutritional benefits!");
        subtitleLabel.getStyleClass().add("text-subtitle");

        // Search Bar
        searchField = new TextField();
        searchField.setPromptText("Type a food (e.g. Strawberry, French Fries)...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(350);
        searchField.setOnAction(e -> performSearch());

        searchBtn = new Button("Search");
        searchBtn.getStyleClass().addAll("btn-base", "btn-primary");
        searchBtn.setPrefWidth(120);
        searchBtn.setOnAction(e -> performSearch());

        HBox searchBox = new HBox(12);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.getChildren().addAll(searchField, searchBtn);

        // Progress/Loading Area
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        progressIndicator.setPrefSize(40, 40);

        // Result Card (Glassmorphism styling)
        resultCard = new VBox(15);
        resultCard.getStyleClass().add("glass-card");
        resultCard.setMaxWidth(600);
        resultCard.setAlignment(Pos.TOP_CENTER);
        resultCard.setVisible(false); // Hidden until search
        resultCard.setPadding(new Insets(20));

        resultTitle = new Label();
        resultTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        badgeLabel = new Label();
        badgeLabel.getStyleClass().add("badge");

        resultImageView = new ImageView();
        resultImageView.setFitWidth(180);
        resultImageView.setFitHeight(180);
        resultImageView.setPreserveRatio(true);

        resultDesc = new Label();
        resultDesc.getStyleClass().add("text-trivia");
        resultDesc.setStyle("-fx-font-size: 14px;");
        resultDesc.setWrapText(true);
        resultDesc.setMaxWidth(550);

        resultCard.getChildren().addAll(resultTitle, badgeLabel, resultImageView, resultDesc);

        // Center card wrapper to hold the loading or results
        StackPane centerContainer = new StackPane();
        centerContainer.getChildren().addAll(progressIndicator, resultCard);
        VBox.setMargin(centerContainer, new Insets(10, 0, 10, 0));

        // Back Button
        Button backBtn = new Button("Back to Main Menu");
        backBtn.getStyleClass().addAll("btn-base", "btn-secondary");
        backBtn.setPrefWidth(220);
        backBtn.setOnAction(e -> controller.showStartScreen());

        backBtn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backBtn);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        backBtn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), backBtn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        layout.getChildren().addAll(titleLabel, subtitleLabel, searchBox, centerContainer, backBtn);
        
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.setCenter(scrollPane);
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        // Reset UI
        resultCard.setVisible(false);
        progressIndicator.setVisible(true);
        searchBtn.setDisable(true);

        // Normalize query for URL (capitalize first letter, replace spaces with underscores)
        String formattedQuery = capitalizeWord(query).replace(" ", "_");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://en.wikipedia.org/api/rest_v1/page/summary/" + URLEncoder.encode(formattedQuery, StandardCharsets.UTF_8)))
                .header("User-Agent", "NutriQuestApp/1.0 (contact@nutriquest.com) JavaFX/21")
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    String body = response.body();
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                        searchBtn.setDisable(false);

                        if (response.statusCode() == 200 && body != null) {
                            displayResult(body, query);
                        } else {
                            displayError("Sorry, could not find '" + query + "' online. Try another food!");
                        }
                    });
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> {
                        progressIndicator.setVisible(false);
                        searchBtn.setDisable(false);
                        displayError("Network error. Please check your connection!");
                    });
                    return null;
                });
    }

    private void displayResult(String json, String originalQuery) {
        String title = extractJsonValue(json, "title");
        String extract = extractJsonValue(json, "extract");
        String imageUrl = extractJsonValue(json, "thumbnail.source");

        if (title == null) title = capitalizeWord(originalQuery);
        if (extract == null) extract = "No detailed description available.";

        // Clean up JSON escaping
        extract = extract.replace("\\\"", "\"")
                .replace("\\n", " ")
                .replace("\\u00a0", " ")
                .replace("\\u2013", "-");

        // Dynamic Food Classifier
        FoodType classification = classifyFood(title, extract);

        resultTitle.setText(title);
        badgeLabel.setText(classification.getDisplayName());
        badgeLabel.getStyleClass().removeAll("badge-good", "badge-bad");
        if (classification == FoodType.GOOD_FOOD) {
            badgeLabel.getStyleClass().add("badge-good");
        } else {
            badgeLabel.getStyleClass().add("badge-bad");
        }

        // Reset image and load dynamically if present
        resultImageView.setImage(null);
        if (imageUrl != null) {
            try {
                // Remove escaping backslashes from JSON URL
                imageUrl = imageUrl.replace("\\/", "/");
                Image img = new Image(imageUrl, true); // load in background
                resultImageView.setImage(img);
            } catch (Exception e) {
                System.err.println("Error loading web image: " + e.getMessage());
            }
        }

        resultDesc.setText(extract);

        // Show card with a fade-in animation
        resultCard.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(300), resultCard);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void displayError(String errorMessage) {
        resultTitle.setText("Oops!");
        badgeLabel.setText("Not Found");
        badgeLabel.getStyleClass().removeAll("badge-good", "badge-bad");
        badgeLabel.getStyleClass().add("badge-bad");
        resultImageView.setImage(null);
        resultDesc.setText(errorMessage);

        resultCard.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(300), resultCard);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private FoodType classifyFood(String name, String description) {
        String lowerName = name.toLowerCase();
        String lowerDesc = description.toLowerCase();

        // 1. Check local mapper first
        for (Map.Entry<String, FoodType> entry : LOCAL_CLASSIFIER.entrySet()) {
            if (lowerName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 2. Keyword heuristic scan
        String[] unhealthyKeywords = {
            "candy", "soda", "sugar", "sweets", "fried", "chips", "pastry",
            "cake", "donut", "syrup", "dessert", "fatty", "processed",
            "fast food", "junk food", "chocolate", "carbonated", "soft drink"
        };

        String[] healthyKeywords = {
            "vegetable", "fruit", "nutrient", "vitamin", "mineral", "protein",
            "fiber", "organic", "calcium", "potassium", "iron", "antioxidant",
            "healthy", "nutritious", "grain", "seafood", "fish", "herb", "spinach"
        };

        int unhealthyScore = 0;
        int healthyScore = 0;

        for (String kw : unhealthyKeywords) {
            if (lowerDesc.contains(kw) || lowerName.contains(kw)) {
                unhealthyScore += 2;
            }
        }

        for (String kw : healthyKeywords) {
            if (lowerDesc.contains(kw) || lowerName.contains(kw)) {
                healthyScore += 1;
            }
        }

        if (unhealthyScore > healthyScore || unhealthyScore >= 2) {
            return FoodType.BAD_FOOD;
        }

        return FoodType.GOOD_FOOD; // Default is good food (e.g. natural fruits/veggies/meats)
    }

    private String capitalizeWord(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.length() > 0) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                  .append(w.substring(1).toLowerCase())
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    private String extractJsonValue(String json, String key) {
        if (json == null) return null;

        // Custom parser logic
        if ("thumbnail.source".equals(key)) {
            int thumbIndex = json.indexOf("\"thumbnail\"");
            if (thumbIndex != -1) {
                String sourcePattern = "\"source\":\"";
                int sourceIndex = json.indexOf(sourcePattern, thumbIndex);
                if (sourceIndex != -1) {
                    int start = sourceIndex + sourcePattern.length();
                    int end = json.indexOf("\"", start);
                    if (end != -1) {
                        return json.substring(start, end);
                    }
                }
            }
            return null;
        }

        String pattern = "\"" + key + "\":\"";
        int index = json.indexOf(pattern);
        if (index != -1) {
            int start = index + pattern.length();
            int end = json.indexOf("\"", start);
            if (end != -1) {
                return json.substring(start, end);
            }
        }
        return null;
    }
}
