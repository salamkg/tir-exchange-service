Задача: Реализовать сервис обмена электронными сообщениями МДП (имитация интеграции с IRU)
Описание:
 Нужно разработать отдельный сервис на Java (Spring Boot), который имитирует обмен сообщениями по процедуре МДП (TIR-EPD) между таможней и IRU.
 Реальной интеграции делать не нужно — всё тестируется через Postman.

 Общие требования
Язык: Java 


Фреймворк: Spring Boot 3.x, желательно Gradle.


Использовать:


spring-boot-starter-web (REST + SOAP)


spring-boot-starter-data-jpa + H2/PostgreSQL


spring-boot-starter-validation


spring-boot-starter-actuator


lombok, mapstruct


springdoc-openapi (Swagger UI)


Формат обмена — SOAP/XML, но предусмотреть REST-обёртку (универсальный контроллер, принимающий XML в теле запроса и возвращающий XML).
 То есть, REST-endpoint принимает XML-payload (имитация шлюза).


Всё тестирование через Postman.



Основная суть
Реализовать микросервис TirExchangeService, который принимает и обрабатывает основные типы сообщений по МДП, описанные в документе IRU (EPD015, EPD028, EPD016, EPD029, EPD051, EPD045).
 Эти сообщения — просто XML-структуры, которые нужно парсить, сохранять и формировать ответ.

 Что конкретно нужно сделать
1. API (веб-уровень)
Сделать REST-контроллер /api/tir/exchange, который принимает XML-сообщение:
POST /api/tir/exchange
 Тело запроса — XML следующего вида (пример EPD015):
<EPD015>
    <GuaranteeNumber>KG12345678</GuaranteeNumber>
    <IruReference>IRU-2025-001</IruReference>
    <VehicleNumber>01KG123ABC</VehicleNumber>
    <HolderNumber>TIRH-998877</HolderNumber>
    <Goods>
        <Item>
            <HsCode>870323</HsCode>
            <Description>Легковой автомобиль</Description>
            <GrossWeight>2500.5</GrossWeight>
            <Packages>1</Packages>
        </Item>
    </Goods>
    <Route>
        <Point>KG-BISHKEK</Point>
        <Point>KZ-ALMATY</Point>
        <Point>RU-OMSK</Point>
    </Route>
</EPD015>

Сервис должен определить тип сообщения (по корневому тегу), распарсить его, провалидировать, сохранить и вернуть XML-ответ (например, EPD028 или EPD016, в зависимости от логики).

2. Логика обработки
Реализовать интерфейс:
public interface TirMessageProcessor {
    String process(String xmlPayload);
}

И несколько реализаций:
Epd015Processor — уведомление EPD


Epd028Processor — присвоение таможенного индекса


Epd016Processor — отклонение


Epd029Processor — разрешение на транзит


Epd051Processor — отказ в транзите


Epd045Processor — завершение


В зависимости от типа входящего сообщения вызывать нужный процессор.
 Ответ возвращать в виде XML (можно использовать JAXB или Jackson XML).

3. Пример внутренней логики
Если GuaranteeNumber начинается с KG → формируем положительный ответ (EPD028).
 Если GuaranteeNumber начинается с XX → формируем отказ (EPD016).
 Если в XML отсутствует HolderNumber → вернуть SOAP Fault-ошибку.

4. Сохранение данных
Создать сущность:
@Entity
@Table(name = "tir_message")
public class TirMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String messageType;
    private String guaranteeNumber;
    private String iruReference;
    private String customsIndex;
    private String status;
    @Lob
    private String payload;
    private LocalDateTime createdAt;
}

Хранить всё в БД ( PostgreSQL).
 Сделать GET /api/tir/messages для просмотра сохранённых сообщений.

5. Ошибки и логирование
Любая ошибка при парсинге XML → вернуть SOAP Fault:


<soap:Fault>
  <faultcode>CLIENT_VALIDATION_ERROR</faultcode>
  <faultstring>Отсутствует элемент GuaranteeNumber</faultstring>
</soap:Fault>

Все входящие и исходящие сообщения логировать в отдельный файл tir-exchange.log.


Добавить обработку исключений через @ControllerAdvice.



6. Конфигурации
Добавить application.yml с параметрами:
server:
  port: 8085
spring:
  datasource:
    url: jdbc:h2:mem:tir;DB_CLOSE_DELAY=-1
  jpa:
    hibernate:
      ddl-auto: update
  jackson:
    serialization:
      indent_output: true
logging:
  file:
    name: logs/tir-exchange.log


7. Swagger
Добавить Swagger UI (springdoc-openapi) и вывести описание методов /api/tir/exchange и /api/tir/messages.

 Что должно работать на выходе
Отправляешь XML через Postman — сервис парсит, валидирует, сохраняет, логирует и возвращает XML-ответ.


Можно открыть Swagger и посмотреть историю сообщений в /api/tir/messages.


Всё должно работать автономно (без шлюзов, без внешних API).
