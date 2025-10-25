package racingcar;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    static class Car {
        private static final int MOVE_CONDITION = 4;
        private static final int MIN_RANDOM_NUMBER = 0;
        private static final int MAX_RANDOM_NUMBER = 9;

        private final String name;
        private int position = 0;

        public Car(String name) {
            Validator.validateCarName(name);
            this.name = name.trim();
        }

        public void move(){
            int randomNumber = Randoms.pickNumberInRange(MIN_RANDOM_NUMBER, MAX_RANDOM_NUMBER);
            if (randomNumber >= MOVE_CONDITION){
                position++;
            }
        }

        public String getName() {
            return name;
        }

        public int getPosition() {
            return position;
        }
    }

    static class Cars {
        private final List<Car> carList;


        public Cars(String[] carNames) {
            this.carList = Arrays.stream(carNames)
                    .map(Car::new)
                    .collect(Collectors.toList());
        }

        public void moveAll(){
            for (Car car : carList){
                car.move();
            }
        }

        public List<Car> getCarList() {
            return List.copyOf(carList);
        }

        private int findMaxPosition() {
            return carList.stream()
                    .mapToInt(Car::getPosition)
                    .max()
                    .orElse(0);
        }

        public List<String> findWinners(){
            int maxPosition = findMaxPosition();
            return carList.stream()
                    .filter(car -> car.getPosition() == maxPosition)
                    .map(Car::getName)
                    .collect(Collectors.toList());
        }
    }


    static class InputView {
        private static final String INPUT_CAR_NAMES_MESSAGE = "경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)";
        private static final String INPUT_TRY_COUNT_MESSAGE = "시도할 횟수는 몇 회인가요?";
        private static final String DELIMITER = ",";

        public static String[] readCarNames() {
            System.out.println(INPUT_CAR_NAMES_MESSAGE);
            String input = Console.readLine();
            return input.split(DELIMITER);
        }

        public static int readTryCount() {
            System.out.println(INPUT_TRY_COUNT_MESSAGE);
            String input = Console.readLine();
            Validator.validateTryCount(input);
            return Integer.parseInt(input);
        }
    }

    static class OutputView{
        private static final String EXECUTION_RESULT_HEADER = "\n실행결과";
        private static final String CAR_POSITION_FORMAT = "%s : %s\n";
        private static final String POSITION_MARKER = "-";
        private static final String WINNER_ANNOUNCEMENT = "최종 우승자 : ";

        public static void printExecutionResultHeader() {
            System.out.println(EXECUTION_RESULT_HEADER);
        }

        public static void printRoundResult(Cars cars) {
            for (Car car : cars.getCarList()) {
                System.out.printf(CAR_POSITION_FORMAT, car.getName(), formatPosition(car.getPosition()));
            }
            System.out.println();
        }

        private static String formatPosition(int position) {
            return POSITION_MARKER.repeat(position);
        }

        public static void printWinners(List<String> winners){
            String winnerNames = String.join(", ",winners);
            System.out.println(WINNER_ANNOUNCEMENT + winnerNames);
        }
    }



    static class Validator {
        private static final int MAX_NAME_LENGTH = 5;

        public static void validateCarName(String name) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("자동차 이름은 공백일 수 없습니다.");
            }
            if (name.trim().length() > MAX_NAME_LENGTH) {
                throw new IllegalArgumentException("자동차 이름은 5자 이하만 가능합니다.");
            }
        }

        public static void validateTryCount(String input){
            try{
                Integer.parseInt(input);
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("시도 횟수는 숫자여야 합니다.");
            }

            if (Integer.parseInt(input) <= 0){
                throw new IllegalArgumentException("시도 횟수는 1 이상의 숫자여야 합니다");
            }
        }
    }


    public static void main(String[] args) {
        try {
            String[] carNames = InputView.readCarNames();
            Cars cars = new Cars(carNames); // cars 객체 생성 (이 과정에서 이름 검증)


            int tryCount = InputView.readTryCount();

            OutputView.printExecutionResultHeader();

            for (int i = 0 ; i < tryCount ; i++){
                cars.moveAll();
                OutputView.printRoundResult(cars);
            }

            List<String> winners = cars.findWinners();
            OutputView.printWinners(winners);

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}