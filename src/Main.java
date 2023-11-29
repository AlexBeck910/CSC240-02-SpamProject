import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author Alex Beck <ab1008897 @ wcupa.edu>
 * @author Kaeli Andrews <ka993962 @ wcupa.edu>
 * @author Mya Bishop <mb994977 @ wcupa.edu>
 */
public class Main {
    public static void main(String[] args) {

        File data = new File("SpamOrNotSpam.csv");
        List<Email> emails = new ArrayList<>();
        try {
            Scanner sc = new Scanner(data);
            while(sc.hasNextLine()) {
                emails.add(Email.fromString(sc.nextLine()));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Processed emails, now starting application");
        Classifier classifier = new Classifier(emails);

        var app = Javalin.create(config -> {
            config.addStaticFiles("./public", Location.EXTERNAL);
        }).start(8080);

        app.post("/classify", ctx -> {
            var response = Objects.requireNonNull(ctx.formParam("email-text"));
            var text = Utils.stripText(response);
            if(Objects.equals(text, " ")) {
                ctx.html("<h3>Please enter text</h3>");
            } else if(Objects.equals(text, "when the imposter is sus")) {
                ctx.html("<img src=\"https://i.kym-cdn.com/photos/images/newsfeed/001/956/027/fee.jpg\">");
            } else {
                System.out.println(text);
                var classification = classifier.classify(text);
                ctx.html(classification.getHtml());
            }
        });
    }
}
