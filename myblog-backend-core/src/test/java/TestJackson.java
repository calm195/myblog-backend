/**
 * @author chrissy
 * @description
 * @date 2024/7/23 23:31
 */
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJackson {
    static class Test{
        public String test;

        @Override
        public String toString(){
            return "Test: " + test;
        }
    }
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // json to class
            String str = "{\"test\":\"json to class\"}";
            System.out.println(objectMapper.readValue(str, Test.class).toString());

            // class to json
            Test a = new Test();
            a.test = "class to json";
            System.out.println(objectMapper.writeValueAsString(a));
        } catch (Exception ex){
            throw new UnsupportedOperationException(ex);
        }
    }
}
