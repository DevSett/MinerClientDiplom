package app.classes.services;

import app.classes.models.Property;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UnknownFormatConversionException;

/**
 * Created by Сергей on 05.07.2017.
 */
@Component("serviceGpu")
@Log4j
public class ServiceGpu {

    @Autowired
    Property property;

    public static void main(String[] args) {
        ServiceGpu serviceGpu = new ServiceGpu();
        serviceGpu.property = new Property();
        serviceGpu.property.init();
        System.out.println(serviceGpu.getInformation());
    }

    public String getInformation() {
        String[] fullInformation;

        String temperature = "";
        String totalMhs = "";
        String mhs = "";
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(property.getPathToLog())), Charset.forName(property.getCharset())));


            String line;

            while ((line = bufferedReader.readLine()) != null) {

                String currentStroke = line;

                if (currentStroke.contains(property.getTemperatureArg())) {
                    temperature = currentStroke;
                }
                if (currentStroke.contains(property.getTotalArg())) {
                    totalMhs = currentStroke;
                }
                if (currentStroke.contains(property.getMhsArg())) {
                    mhs = currentStroke;
                }
//                if (!temperature.isEmpty() && !totalMhs.isEmpty() && !mhs.isEmpty()) break;
            }
        } catch (
                FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (
                IOException e)

        {
            log.error(e.getMessage(), e);
        }

        if (temperature.isEmpty()) {
            temperature = "Температура не найдено!";
        }
        if (totalMhs.isEmpty()) {
            totalMhs = "Общее кол-во мощности не найдено!";
        }
        if (mhs.isEmpty()) {
            mhs = "Кол-во мощности каждой видеокарты не найдено!";
        }


//        return mhs;
        return "\uD83D\uDD6F\uD83D\uDD6F\uD83D\uDD6F" + property.getName() + "\nТемпература:\n" + parse(temperature) +
                "\nОбщая мощность: \n" + parse(totalMhs) + "\nМощность: \n" + parse(mhs);

    }

    private String parse(String stroke) {
        switch (property.getType()){
            case "0":{
                if (stroke.split("\t").length < 2) return  "Некорректная обработка!";
                if (stroke.split("\t")[2].indexOf(",") == -1) return  "Некорректная обработка!";


                return "Дата: " + stroke.split("\t")[0] + "\n" + stroke.split("\t")[2].replaceAll(",", "\n") + "\n";
            }
            case "1":{
                return stroke;
            }
            default: return "Некорректная обработка!";
        }

    }
//    public String formatLineLog(String line, int fistSymbol, int secondSymbol, String symbol) {
//        return line.substring(line.indexOf(symbol, fistSymbol) + 1, line.indexOf(symbol, secondSymbol));
//    }

    public String getLastTen() {
        String[] strokes = getLastTenStrokeDebug();
        String result = "";
        if (strokes == null) return result;

        for (String stroke : strokes) {
            result += stroke + "\n";
        }
        return "\uD83D\uDEE0\uD83D\uDEE0\uD83D\uDEE0" + property.getName() + "\n" + result;
    }

    private String[] getLastTenStrokeDebug() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(property.getPathToLog()));
            String[] lastTenStrokes = new String[10];

            String line = null;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (i != 10) lastTenStrokes[i++] = line;
                else {
                    for (int i1 = 1; i1 < lastTenStrokes.length; i1++) {
                        lastTenStrokes[i1 - 1] = lastTenStrokes[i1];
                    }
                    lastTenStrokes[i - 1] = line;
                }
            }
            if (lastTenStrokes[0] == null) return null;
            return lastTenStrokes;

        } catch (IOException e) {
            log.error("textFromFile", e);
        }
        return null;
    }

    public InputStream getScreenshot() {
        BufferedImage bufferedImage = screenshot();
        if (bufferedImage == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }

    private BufferedImage screenshot() {
        try {
            BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "png", new File("screenshot.png"));
            return image;
        } catch (AWTException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
