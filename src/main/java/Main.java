import java.util.*;
import java.util.stream.*;
import java.io.Console;
import domain.*;

public class Main {
    /** 상수 영역 */
    private final static int LOTTO_NUM_START = 1;
    private final static int LOTTO_NUM_END = 45;
    private final static int LOTTO_NUM_LENGTH = 6;
    private final static int LOTTO_PRICE = 1000;
    private final static Random random = new Random();
    private final static String INPUT_INT_ERROR
            = "올바른 숫자가 입력되지 않았습니다. 다시 입력해 주세요.";
    private final static String INPUT_WIN_NUMBERS
            = "지난 주 당첨 번호를 입력해 주세요.";
    private final static String INPUT_BONUS_BALL
            = "보너스 볼을 입력해 주세요.";
    
    /** 로또 하나를 뽑아서 Lotto 객체로 반환 */
    private static Lotto getLotto() {
        IntStream stream = random.ints(LOTTO_NUM_START, LOTTO_NUM_END + 1);
        List<Integer> temp = stream
                .limit(LOTTO_NUM_LENGTH * 2) // 중복 때문에 2배수로 뽑음
                .sorted()
                .distinct()
                .limit(LOTTO_NUM_LENGTH)
                .boxed() // IntStream to Integer
                .collect(Collectors.toList());
        return new Lotto(temp);
    }
    
    /** 문자열을 입력받아, 쉼표를 기준으로 나누어 문자열 배열로 반환 */
    private final static String[] getInput(String message) {
        Console console = System.console();
        String inputData = console.readLine(message + "\n"); // 메시지 출력
        return inputData.split(",\\s*"); // 정규 표현식 적용 - 띄어쓰기 때문
    }
    
    /** 입력된 문자열을 정수로 형변환할 수 있을지 테스트 */
    private final static boolean isOnlyNumber(String string) {
        if (string.length() == 0) {
            return false; // 빈 문자열은 취급하지 않는다.
        }
        return string.chars() // String -> IntStream
                .map(i -> (i - '0')) // 형변환 발생
                .noneMatch(i -> (i < 0 || i > 9)); // 0부터 9 사이의 숫자인가
    }
    
    /** 임의의 정수가 로또 번호 범위 안에 있는지를 확인 */
    private final static boolean isLottoRange(int number) {
        return (number >= LOTTO_NUM_START) && (number <= LOTTO_NUM_END);
    }
    
    /** 임의의 정수 하나를 입력받아 반환한다. */
    private final static int inputSingleInt(String message) {
        String temp = getInput(message)[0];
        boolean check = isOnlyNumber(temp);
        while (!check) { // 입력에 문제가 있으면 계속 재입력 요구
            temp = getInput(INPUT_INT_ERROR)[0];
            check = isOnlyNumber(temp);
        }
        return Integer.parseInt(temp);
    }
    
    /** 로또 번호를 입력받으려고 1번 시도한다. */
    private final static List<Integer> tryInputLottoNums(String message) {
        Stream<String> temp = Arrays.stream(getInput(message));
        return temp
                .filter(Main::isOnlyNumber) // 숫자만 거름
                .map(Integer::parseInt) // 정수로 변환
                .filter(Main::isLottoRange) // 범위 확인
                .distinct() // 중복 제거
                .sorted() // 오름차순 정렬
                .collect(Collectors.toList()); // stream을 List로 변환
    }
    
    /** 로또 번호를 입력받아 Lotto 객체를 반환한다. */
    private final static Lotto inputLottoNums(String message) {
        List<Integer> temp = tryInputLottoNums(message);
        while (temp.size() != LOTTO_NUM_LENGTH) { // 문제가 없을 때까지 반복
            temp = tryInputLottoNums(INPUT_INT_ERROR);
        }
        return new Lotto(temp);
    }
    
    /** 지난 주 당첨 번호를 입력받아 WinningLotto 객체를 반환한다. */
    private final static WinningLotto lastWeekWinningLotto() {
        Lotto winning = inputLottoNums(INPUT_WIN_NUMBERS);
        int bonusNo = inputSingleInt(INPUT_BONUS_BALL);
        while (!isLottoRange(bonusNo) || winning.hasNumber(bonusNo)) {
            bonusNo = inputSingleInt(INPUT_INT_ERROR);
        }
        return new WinningLotto(winning, bonusNo);
    }
    
    /** main 진입점 */
    public final static void main(String[] args) {
        // Test code - 추후 삭제 예정
        // List<Integer> lottoNums = Arrays.asList(1, 2, 3, 4, 5, 6);
        // Lotto lotto = new Lotto(lottoNums);
        // System.out.println(lotto);
        // Lotto lotto = inputLottoNums();
        // System.out.println(lotto);
        // WinningLotto winner = lastWeekWinningLotto();
        // System.out.println(winner);
    }
}
