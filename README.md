# my:Store)

[![Build Status](https://travis-ci.org/JIeIIIa/mystore.svg?branch=master)](https://travis-ci.org/JIeIIIa/mystore)
[![Coverage Status](https://coveralls.io/repos/github/JIeIIIa/mystore/badge.svg?branch=master)](https://coveralls.io/github/JIeIIIa/mystore?branch=master)

## Тестове завдання на знання Java EE (servlets) та Javascript/jQuery.

Створити веб-додаток mystore використовуючи Java Servlets.

Додаток має повертати веб сторінки з статичних HTML файлів в WAR-архіві (самі файли недоступні з зовні) 
по наступним URL відносно серверу:

| Файл| Відносний URL |
|----|----|
| home.html | /mystore |
| shop.html | /mystore/shop |
| success.html | /mystore/shop/success |
| failure.html | /mystore/shop/failure |

### Бекенд

 * **_Каталог товарів_** (назва, артикул, ціна) - внутрішня колекція що заповнюється при ініціалізації 
   сервлета `/mystore/shop/items` з csv-файлу в папці `<catalina.home>/data` серверу Tomcat. 
   Каталог має регулярно оновлюватися (замінюватися) окремим процесом з файлу даних кожні 5хв. 
   Вміст каталогу завжди доступний для обробки запитів кліентів. 
   Каталог завжди повністю відповідає вмісту конкретного файлу даних - 
   весь асортимент та ціни лише з одного, поточного, файлу.
   
 * **_Сервлет+JSP_**: `/mystore/shop/items` - читає весь доступний товар з каталогу та 
   повертає **HTML** для відображення на кліенті. 

 * **_Сервлет+JSP_**: `/mystore/shop/basket` - сторінка підтвердження покупки, відображає обрані 
   товари (один чи декілька) з запиту кліента. Сторінка попередньо валідує запит з каталогом
   перед поверненням респонсу, містить кнопку **_Buy_**, в разі якщо запит містить не існуючий 
   артикул має відобразити артикул червоним та не відображати решту даних по такому товару.
   Неуснуючі артикули не використовувати в покупці.
   
 * **_Сервлет_**: `/mystore/buyService` - приймає запит з списком артикулів, валідує за каталогом, 
   та зберігає їх в файл серверу '/data/order-TIMESTAMP.csv', де 
   _TIMESTAMP_ - поточний час на сервері.
   Валідацію і дані в файл проводити на основі даних, які є на сервері на момент запиту. Ситуацію, коли
   у клієнта бачив, наприклад, вартість А, а на момент покупки вартість стала В (т.я. каталог товарі періодично
   оновлюється) не враховуємо.    
   В разі успішного створення файлу сервлет повертає статус `201`, 
   в разі не існуючого товару `400`, 
   в разі іншої помилки `500`.
   Сюди товар має прийти тіки той що є в каталозі, але якщо інше - то всю покупку відхиляємо.

### Фронтенд

Контент сторінок створи довільний. 

* Сторінка `/mystore` має лінк на `/mystore/shop`. 

* Треба використати **Javascript** та **jQuery** для того щоб завантажи на `/mystore/shop` контент 
  з URL `/mystore/shop/items` в `<div>` елемент на цій сторінці (декілька товарів з назвами).
  Іншими словами фрагмент **HTML**, який отримуємо в результаті **jQuery** запиту потрібно 
  вставити на сторінку `/mystore/shop` в `<div>`
 
* На `/mystore/shop` користувач може вибирати (через checkbox) з завантаженних `/mystore/shop/items` 
  товарів та, натиснувши кнопку **_To Basket_**, завантажити в поточному вікні сторінку 
  `/mystore/shop/basket`, де обрані елементи будуть передані в запиті як 
  `/mystore/shop/basket?items=art1,art2,...,artN`.

* Сторінка `/mystore/shop/basket` має кнопку **_Buy_**, при натисканні якої виконується 
  асинхроний запит до сервлету `/mystore/buyService` методом `POST`. 
  Сервлет має повернути статус `201` в разі успіху. 
  Сторінка має розрізняти статуси та в разі `201` перейти на `/myapp/shop/success`, 
  якщо повернуто помилку то на `/mystore/shop/failure`.

* На сторінках `/mystore/shop/success` та `/mystore/shop/failure` відображати 
  просто дружній меседж користувачеві.

Стиль сторінок оформи простим **CSS** - вирівнювання та відступи, 
щоб було приглядно на різних розмірах сторінки (responsive по можливості).

## Prerequisites
For building and running the application you need:
* [JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
* [Git Guide](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* Maven 3.5.3 or later ([Installing Apache Maven](https://maven.apache.org/install.html))
* [Tomcat 8](https://tomcat.apache.org/download-80.cgi) or later

## Installing

### Clone repository
Clone this repository onto your local machine. You can do it like this:
```shell
$ git clone https://github.com/JIeIIIa/mystore
```

### Compile
Open a terminal, change a directory to the project root directory, run:
```shell
$ mvn clean install
```
After that you will see `mystore.war` at `<project root directory>/target`

## Running the application using the command-line

This project can be built with [Apache Maven](http://maven.apache.org/).

Use the following steps to run the application locally:

1. Execute next Maven goals to create the `target/mystore.war` file:
   ```bash
   $ mvn clean install
   ```
2. Deploy the created file `mystore.war` by simply dropping it into 
   the `$CATALINA_HOME\webapps` directory of any Tomcat instance, 
   where `$CATALINA_HOME` is the Tomcat’s installation directory.
3. By default the application will be available at `http://localhost:8080/mystore`

## Troubleshooting 

* Make sure that you are using java 8, and that maven version is appropriate.
  ```shell
  mvn -v
  ```
  should return something like:
  ```
  Apache Maven 3.5.3
  Maven home: C:\Program Files\Maven\bin\..
  Java version: 1.8.0_192, vendor: Oracle Corporation
  Java home: C:\Program Files\Java\jdk1.8.0_192\jre
  ```

* Make sure that Tomcat is running. 
  We can start the Tomcat server by simply running the startup script located 
  at `$CATALINA_HOME\bin\startup`. There is a .bat and a .sh in every installation.                                    
  Choose the appropriate option depending on whether you are using a Windows 
  or Unix based operating system.
  
* Make sure that the context path of the application is `/mystore`