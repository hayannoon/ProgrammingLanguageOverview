# ProgrammingLanguageOverview
This is my homework for Programming Language Overview class
Languate : Java
알고리즘 문제부터 시작해서, Cute19 Interpreter를 java로 구현하게 되는 과정이다.

<h4>HW01</h4> - 1)Double factorial problem<br>
     - 2)Faery sequence problem<br>
<h4>HW02</h4> - Making linked list problem<br>
HW03 - Recognizing Tokens problem<br>
HW04 - Cute19 Scanner : cute19 문법에 따라서 작성된 text파일을 input으로 받고,<br>
       모든 token을 인식하여 token과 lexeme을 출력하여 파일에 저장하는 프로그램<br>
HW05 - Jar파일은 문자열을 input으로 받아서 Node로 리턴하게된다. 이떄 작성된 문자열이<br>
       List 또는 Int만 들어있다고 가정하고, 최대값과 총 합을 구하는 메소드를 작성한다.<br>
       예를들어 ( ( 3 2 ) -378 ( ) ) 의 경우 최대값:3 , 총합:-373 을 출력한다.<br>
HW06 - Cute19 문법에 따라 작성된 program이 text 파일 형식으로 저장되어있는데, <br>
       이를 input file로 하여, 프로그램의 syntax tree를 구성하고, token과 lexeme을<br>
       파일에 출력하는 program. 예를들어 input이 ( + ( - 3 2 ) -378 ) 일 경우,<br>
       output은 ([PLUS] ([MINUS] [INT:3] [INT:2]) [INT:-378]) 이런식으로 나오게 한다.<br>
HW07 - Cute19 문법에 따라 작성된 파일이 text파일 형식으로 저장되어있고, 이를 input으로해서<br>
       프로그램의 syntax tree를 구성하고, syntax tree를 root로부터 pre-order traverse하여<br>
       원래 입력된 프로그램과 구조가 동일한 프로그램을 파일에 출력한다.(unparsing)<br>
       예를들어 ( + ( - 3 2 ) -378 ) 입력이 들어온다면,<br>
       ( [PLUS] ( [MINUS] [INT:3] [INT:2] ) [INT:-378] ) 출력이 나와야 한다.<br>
HW08 - Cute 19 문법에 따라 작성된 파일이 text파일 형식으로 저장되어있고, Cute19의<br>
       built-in function을 사용해서 리스트를 조작하여 그 결과를 구한다. 그리고<br> 
       input 파일의 syntax tree를 출력한다. 예를들어,<br>
       > ( car ‘ ( 2 3 4 ) ) 의 output은 2<br>
       > ( cdr ‘ ( 2 3 4 ) ) 의 output은 '( 3 4 )<br>
       > ( cons 1 ‘( 2 3 4 ) ) 의 output은 '( 1 2 3 4 )<br>
       > ( null? ‘ ( ) ) 의 output은 #T<br>
       > ( atom? ‘ a ) 의 output은 #T<br>
       > ( eq? ‘ a ‘ a ) 의 output은 #T <br>
       그리고 기타 산술연산, 관계연산, 논리연산, 조건문까지 알맞은 output을 출력한다.<br>
    <br>
ITEM1 - HW08까지 완성된 프로그램을 file input에서 console input으로 변환한다.<br>
ITEM2 - 1)변수에 대한 define문을 처리한다. 예를들어 ( define a 1 ) 이라고 하면,<br> 
        a의 값이 1임을 저장하는 심벌 테이블을 만든다. 이때 ( define b ' ( 1 2 3 ) ) 등의 <br>
        리스트 형태가 입력될 수도 있고, ( define c ( - 5 2 ) ) 와같은경경우에는 c에 -3이 저장된다.<br>
      - 2)변수의 값을 사용할 수 있도록 한다. 다시말해 ( + a 3 ) 이라고 했을때, define으로 a값이 2로<br>
        정의되었다고 가정하면 결과값이 2 + 3 인 5가 나오게된다. 그리고 같은 변수가 두번 이상 정의되면<br>
        마지막에 정의된 값을 갖게된다.<br>
ITEM3 - 함수의 바인딩 처리 및 코드 발전. 함수의 정의도 define으로 가능하다.<br>
        예를들어 (define plus1 ( lambda ( x ) ( + x 1 ) ) 으로 함수를 정의한 뒤,<br>
        ( plus1 2 ) 를 입력하면 3이 출력된다.
