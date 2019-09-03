package btc.da;

public class TestUtils {




    public static Person defaultPerson(){
        return person("name", 10);
    }


    public static Person person(String name, int height){
        return new Person(name, height);
    }






    public static class Person {

        private String name;
        private Integer height;


        public Person() {
        }

        public Person(String name, Integer height) {
            this.name = name;
            this.height = height;
        }

        public String getName() {
            return name;
        }

        public Integer getHeight() {
            return height;
        }
    }
}
