package racingcar;

import camp.nextstep.edu.missionutils.Console;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {

    static class Car {
        private final String name;
        private int position = 0;

        public Car(String name) {
            Validator.validateCarName(name);
            this.name = name.trim();
        }
    }

    static class Cars {
        private final List<Car> carList;


        public Cars(String[] carNames) {
            this.carList = Arrays.stream(carNames)
                    .map(Car::new)
                    .collect(Collectors.toList());
        }
    }


    static class InputView {
        private static final String INPUT_CAR_NAMES_MESSAGE = "경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)";
        private static final String DELIMITER = ",";

        public static String[] readCarNames() {
            System.out.println(INPUT_CAR_NAMES_MESSAGE);
            String input = Console.readLine();
            return input.split(DELIMITER);
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
    }


    public static void main(String[] args) {
        try {
            String[] carNames = InputView.readCarNames();
            Cars cars = new Cars(carNames); // cars 객체 생성 (이 과정에서 이름 검증)

            System.out.println("1단계 성공");

        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}