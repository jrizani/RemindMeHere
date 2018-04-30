package id.juliannr.remindmehere;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import id.juliannr.remindmehere.util.DateHelper;
import ru.ztrap.iconics.IconicsStringGenerator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ExampleUnitTest extends IconicsStringGenerator {

    @Test
    public void generateGoogleIcon() throws TransformerException, ParserConfigurationException {
        generateIconsFrom(new GoogleMaterial());
        generateIconsFrom(new MaterialDesignIconic());
    }

    @Override
    protected FileCreationStrategy fileCreationStrategy() {
        return FileCreationStrategy.SAVE_ONLY_CURRENT;
    }

    /**
     * @return modifier for mark file as current-version file
     * */
    protected String modifierCurrent() {
        return "_current_";
    }

    /**
     * @return directory path for generated .xml file
     * */
    protected String outputDirectory() {
        return "app" +File.separator+ "src" + File.separator + "main" + File.separator + "res" + File
                .separator +
                "values";
    }

    @Test
    public void testTanggal(){
        try{
            Date date;
            String theDate = "5/2/2018";
            System.out.println(DateHelper.toDueDate(theDate));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}