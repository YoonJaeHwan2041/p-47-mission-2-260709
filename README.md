# 먼저든 생각
- Quote for문돌리면서 비슷한 문자열 검색하기
- 분기를 어떻게 나누지?
- 현재 자르는 방식은 키워드와 너무 맞지않다.
- 어떻게 나눌까

## 어떻게 나눌고 구분할까
- 기존 방식은 "?id="전체를 잘라서 "삭제", "1"이런식으로 명령어와 id값만 남게 했다.
- 하지만 지금은 키워드타입, 키워드, 그에대한 답들 이 필요하다.
- 계산기에서 했던것처럼 StringTokenizer로 나눌까?
- 어쩌피 순서는 똑같으니깐 split으로 다 자르고 시작할까?
- 꼭 키워드타입, 키워드 + 그에대한 답들을 다 가져가야할까? / 만약 검색만, 타입만 할수도 있기에 이건 해야할듯
- 만약 한다면 키워드, 키워드 타입 따로 입력한것 , 같이 입력한것 의 분기는 어떻게 처리할건지

### 문자열 나누기 첫시도
- 받아야 할값dms 명령어, 키워드 타입, 키워드 타입에 대한답, 키워드, 키워드에 대한 답
- 지금 명령어의 경우 절대적으로 [0]이자리 이건 넘긴다.
- 문제는 키워드 타입, 키워드 타입에 대한답, 키워드, 키워드에 대한 답
- 우선 답을보자 명령) 목록?keywordType=content&keyword=과거
- Sting[]으로 나눈다
- 지금은 "?id=" 이다.
- ?, = ,& 으로 나누자
- 그럼 남는건
  - [0] = 목록
  - [1] keywordType
  - [2] content
  - [3] keyword
  - [4] 과거
  - 이렇게 된다.
- 무지성으로 for문을 돌려서 변수에 넣는다
- 다음은 분기다.
- 분기를 어떻게 처리할까
- 우선 키워드가 있으면 넘길까?
- 근대 나중에 삭제에 키워드가 있으면?
- 그럼 목록과 키워드가 같이있으면 case넘어가는걸로 하나를 더만들자

#### 추가적인 문제
- 추후 파라미터가 훨신 많아지면 if지옥

### Map?
- 지금보니깐 결국 키, 값이다
- 키 = keywordType, 값 = content

#### 어케 나눌거임?
- 생각해보면 어쩌피 for문 돌리거나 if난사하거나 코드길이 길어지는건 똑같은데 나누는것도 여러번 시도
- 자르기순서 목표 : 목록?keywordType=content&keyword=과거
- ? 로 나눔(근대 책재목이 ? 가 들어가면?) / 최대 자르는수 2개로 줘서 이후 안자르게
- 목록, keywordType=content&keyword=과거
- 목록은 내비두고 2번쨰꺼를 &로 자름
- keywordType=content, keyword=과거
- 각각 나눠진걸 =를 나눔
- keywordType, content, keyword, 과거 : 내가 원하는 대로 짤림

##### 자르기 시도
```java
// 자르기 시도
        //Key Value로 담을 Map 생성
        Map<String, String> params = new HashMap<>();
        //챕터 1 : 가장먼저 2분할
        String[] parts = command.split("\\?", 2);
        String action = parts[0];
        // 챕터2 : &퍼센트가 있을시 반으로 가름
        String[] parts2 = parts[1].split("&");
        // 챕터3 : 키, 벨류 분리
        for (String pair : parts2){
            String[] keyValue = pair.split("=");
            if(keyValue.length == 2){
                String key = keyValue[0];
                String value = keyValue[1];
                params.put(key,value);
            }
        }
```
우선 생각나는대로 자르기만했다. 여기서 오류가 뜰상황이나 돌발 상황에 대해서 생각해보자
우선 생각나는 예시를 먼저 보자
정상 버전
- 수정?id=2;
  - 챕터 1 : 수정, id=1
  - 챕터 2 : &없으니 그양 id = 1 반환
  - 챕터 3 : key = id, value = 1;
  - 문제점 : 변수 만들기만하고 Map에 안넣음
  - 해결 : key,value값을넣은 바로 아래에 `params.put(key,value);`  추가
  - params에 id, 1 들어가는걸 확인함
- 목록?keywordtype=content&keyword=다람쥐
  - 챕터 1 : 목록, keywordtype=content&keyword=다람쥐
  - 챕터 2 : keywordtype=content, keyword=다람쥐
  - 챕터 3 : params[keywordtype, content], params[keyword, 다람쥐] 들어간거 확인
오류 버전
- 목록=keyword?id=1?keyword=2
  - 챕터 1 : 목록=keyword, id=1?keyword=2
  - 챕터 2 : &없어서 그대로 반환
  - 챕터 3 : =이 3개 이상이라 바로 for문 나가버림
  - 결론 : 잘못된 명령어로 빠짐 

우선 이정도로 해보았다.
나는 하나하나 디버깅으로 했지만 나중에 testcase를 잘쓰게되면 좀더 편할거같다.

해당 테스트로 나온 결론은 우선 과제 내용에서 오타는 대부분 오류로 빠지기때문에 크게 문제는 없어보인다.

##### 오류
- 등록 등 자르지 않는 값들을 입력하면 오류가 뜸
- java.lang.ArrayIndexOutOfBoundsException:
- 맨 첫번쨰 스플릿시 ?로 스플릿을한다.
- 하지만 ? 가 없을시 배열이 1개 뿐인데, 밑에서 *로 자를때 [1]번째 배열을 찾으니 해당 오류가 난것이다.

##### 해결
- if문으로 parts 길이가 1초과일때만 해당 작업을 진행하게 했다.

### stream 을 이용한 가독성 + 리스트로 넘기기
- 이전 프로젝트에서 for문과 if를 계속조합할거면 stream을 쓰는게 좋다라고 많이 들었다.
- 그래서 stream으로 카테고리나 상품의 데이터를 뽑아오는 일이 많아 이걸 쓰기로했다.
- 그때도 팀원이 하던거 그대로 따라쓰기만하고 이해는 잘못했지만 이번기회에 어느정도는 느낌이왔다.,
- filter의 경우 말그대로 if문이라고 생각하면된다.
- 람다는 넘어가자
- filter로 if문을 걸고 그안에 하나더 if문을 추가한다. 만약 키워드가 없을시 전체적인걸 목록을 보여준다
- 아닐시 target 변수에 작가일시 이번 반복의 quote(대충 인덱스 = 0 이라고 생각하자)의 작가 데이터를넣고 아니면 컨텐츠 데이터를넣는다
- contatins변수를 사용하여 true인지 false인지 확인하고 true면 collect에 값을 넣는다 아닐시 다시 반복

  
# 페이징처리
- Spring boot 의 Page를 사용한적만 있어서 직접 페이징 처리는 처음
- 근대 메타데이터등 필요없어보여서 간단하게 구현 가능해보임
- 대충 생각나는거
- 페이지 총갯수 = (size - 1) / 5 +1
- for문 안쓰고 어떻게든 stream으로 할려함
- 근대 ai가 true false형식이라 내가 생각 하는 방식 안된다함
- 근대 이것도 선배님들이 생각을 했을테니 없을리가 없으니 더찾아봄

## stream 
- 우선 필요한걸 찾는다
- filter : 조건, true를 반환
- map : 데이터 변형, 지금당장은 필요없음
- sorted : 정렬, 최신순으로 써야하므로 필요함
- distinct : 중복제거, 지금은 필요없음
- flatMap : ?? 우선 필요없음
- Max, Min, Count : 최댓값 , 최소값, 갯수 비어있다면 Optional반환?
- skip : 처음 n개의 요소를 버림 filter이후에 작성
- limit : 설정한 값 이상을 반환하지 않음

### 시행착오
- stream 의 속성도 순서가 중요함
- skip, limit sorted하니깐 이상하게 순서가나옴
```java
public List<Quote> getList(String keywordType, String keyword) {

        List<Quote> quotes = quoteRepository.findAll();
        int a = 0;
        int totalPage = (quotes.size() -1) / 5 + 1;
        return quotes.stream()
                .sorted(Comparator.comparing(Quote::getId))
                .skip(Integer.parseInt(keyword))
                .limit(5)
                .collect(Collectors.toList());
    }
```
- 목록만 누를시 keyword가 없는데 숫자로 변환하니 NumberFormatException 이 나왔다. 
- 미리 try catch로 잡기로함
- 잘해 놓고 skip에 page를 안넣음 이걸 10분간 찾음
- 정렬도 잘못함;
- Controller에서는 keyword를 "keyword"만 받고있어서 당연히 안되는거였음;
- .sorted(Comparator.comparing(Quote::getId).reversed()) 역순 정렬
- 목록 은 정상 출력, 목록?page=2 할시에는 전체 출력 및 정렬 안됨
- keyword,keywordtype의 문자열을 key로 사용하여 value값을 넣는 중이라 문제라 생각됨
- 07/21에 배웠던 내용으로 변경
- totalPage를 5개만 있는 quotes로 해서 제대로 출력이 안되 service에서 repository count를 이용하여 totalpage를따옴
- page는 1부터 시작하는데, skip은 "몇 개를 건너뛸지"라 0부터 계산해야함
- 그냥 page*5 하면 1페이지도 5개를 건너뛰어버려서 한 페이지씩 밀려버림 (2페이지 눌렀는데 3페이지 내용이 나옴)
- 그래서 한 페이지 분량(5)만큼 빼줘서 보정함

# 참고 문서
- 문자열 검색 :https://coding-factory.tistory.com/534
- split : https://jamesdreaming.tistory.com/84
- 리스트 검색 : https://recordsoflife.tistory.com/327
- stream : https://isntyet.github.io/java/java-stream-%EC%A0%95%EB%A6%AC(filter)/
  - https://develop-writing.tistory.com/137 1/2/3 번들
  - https://observerlife.tistory.com/5