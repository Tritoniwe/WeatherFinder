import exceptions.RunException
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.*;

/**
 * Created by Triton on 14.04.2015.
 */
class ConsoleTest {
    static def mainWorker;
    def args;


    @BeforeClass
    public static void test_start(){
        mainWorker=new MainWorker();
    }
    @Test
    public void empty_or_not_full_parameters(){

        args = ["-name",];
        try{
            mainWorker.checkInput(args);
            fail();
        }catch (RunException e){
            assertEquals("",e.getMessage());

        }
        args = ["-asdasd asdas",]
        try{
            mainWorker.checkInput(args);
            fail();
        }catch (RunException e){
            assertEquals("Wrong arguments! [-asdasd asdas]",e.getMessage());

        }
        args = ["-c",]
        try{
            mainWorker.checkInput(args);
            fail();
        }catch (RunException e){
            assertEquals("",e.getMessage());

        }
    }

    @Test
    public void wrong_coordinates() {
        def list = new ArrayList();
        list.add(["-c", "91.554,45.568"]);
        list.add(["-c", "90.010,180.000"]);
        list.add(["-c", "90.000,180.100"]);
        list.add(["-c", "90.ds,180.000"]);
        list.add(["-c", "45.1,45.1ssd"]);
        list.add(["-c", "45.1as,45.1"]);
        list.add(["-c", "45.43834235234252345,46.538"]);
        list.add(["-c", "45.438342,180.538"])
        for (def args : list) {
            try {

                mainWorker.checkInput(args);
                fail("Different variants of wrong coordinates");

            } catch (RunException e) {
                assertEquals("Wrong format of coordinates. Please check your input. Example -c --38.453,46.455", e.getMessage());

            }
        }
    }



    @Test
    public void correct_parameteres(){
        assertEquals("90.000,180.000",mainWorker.checkInput(["-c","90.000,180.000"]));
        assertEquals("90,180",mainWorker.checkInput(["-c","90,180"]));
        assertEquals("45.1,45.1",mainWorker.checkInput(["-c","45.1,45.1"]));
        assertEquals("45.438,46.538",mainWorker.checkInput(["-c","45.438,46.538"]));
        assertEquals("45.438342,46.538",mainWorker.checkInput(["-c","45.438342,46.538"]));
    }

}
