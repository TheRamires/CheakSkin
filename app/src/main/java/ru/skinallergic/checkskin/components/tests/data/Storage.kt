package ru.skinallergic.checkskin.components.tests.data

import ru.skinallergic.checkskin.R
import ru.skinallergic.checkskin.utils.*
import javax.inject.Inject

class Storage @Inject constructor(){
    val tests : List<EntityTest> = arrayListOf(
            TEST_0.entity,
            TEST_1.entity,
            TEST_2.entity,
            TEST_3.entity,
            TEST_4.entity,)

    object TEST_0{
        val id: Long=0
        val name ="Скрининговый опросник PEST"
        val image= R.mipmap.test_image_00

        private val answers0: List<Answer> = arrayListOf(
                Answer(1, "Да"),
                Answer(0, "Нет"))

        private val listQuestions: List<Question> = arrayListOf(
                Question(0,"Была ли у Вас когда-нибудь припухлость сустава (суставов)?",answers0),
                Question(1,"Говорил ли Вам когда-нибудь врач, что у Вас артрит?", answers0),
                Question(2,"Отмечали Вы когда-либо на ногтях пальцев рук или ног какие-нибудь изменения в виде " +
                        "точечных углублений, либо неровности?", answers0),
                Question(3,"Отмечали ли Вы когда-нибудь боль в области пятки?", answers0),
                Question(4,"Были ли у Вас когда-нибудь без видимых причин равномерное припухание и болезненные " +
                        "пальцы рук или ног?", answers0),
        )

        private val result0 = Result(0, 3, 100,"Вам нужно обратиться в врачу-ревматологу для осмотра и " +
                "дополнительного обследования, так как у Вас есть косвенные признаки псориатического артрита.","" )
        private val result1 = Result(1, 0, 2,"На данный момент явных признаков псориатического артрита у вас нет. Внимательно относитесь к своему здоровью и " +
                "регулярно проходите профилактические осмотры.", "")

        private val results : List<Result> = arrayListOf(result0, result1)

        val entity = EntityTest(id, name,
                "Предназначен для раннего выявления признаков псориатического артрита при псориазе.",
                "В каждом вопросе выберите одно из утверждений, которое наиболее вам подходит.",
                image, listQuestions, 0, 2,results)
    }

    object TEST_1{
        val id: Long=1
        val name ="Оценка уровня депрессии"
        val image= R.mipmap.test_image_01

        private val answers0: List<Answer> = arrayListOf(
                Answer(3, text = "Это совсем не так"),
                Answer(2, "Лишь в очень малой степени, это так"),
                Answer(1, "Наверное, это так"),
                Answer(0, "Определенно, это так"))

        private val answers1: List<Answer> = arrayListOf(
                Answer(3, text = "Определенно, это так"),
                Answer(2, "Наверное, это так"),
                Answer(1, "Лишь в очень малой степени, это так"),
                Answer(0, "Совсем не способен"))

        private val answers2: List<Answer> = arrayListOf(
                Answer(3, text = "Совсем не испытываю"),
                Answer(2, "Очень редко"),
                Answer(1, "Иногда"),
                Answer(0, "Практически все время"))

        private val answers3: List<Answer> = arrayListOf(
                Answer(3, text = "Практически все время"),
                Answer(2, "Часто"),
                Answer(1, "Иногда"),
                Answer(0, "Совсем нет"))

        private val answers4: List<Answer> = arrayListOf(
                Answer(3, text = "Определенно, это так"),
                Answer(2, "Я не уделяю этому столько времени, сколько нужно"),
                Answer(1, "Может быть, я стал(а) меньше уделять этому времени"),
                Answer(0, "Я слежу за собой также, как и раньше"))

        private val answers5: List<Answer> = arrayListOf(
                Answer(3, text = "Совсем так не считаю"),
                Answer(2, "Значительно меньше, чем обычно"),
                Answer(1, "Да, но не в той степени, как раньше"),
                Answer(0, "Точно так же, как и обычно"))


        private val answers6: List<Answer> = arrayListOf(
                Answer(3, text = "Очень редко"),
                Answer(2, "Редко"),
                Answer(1, "Иногда"),
                Answer(0, "Часто"))

        private val listQuestions: List<Question> = arrayListOf(
                Question(0,"То, что приносило мне большое удовольствие, и сейчас вызывает у меня такое же " +
                        "чувство", answers0),
                Question(1,"Я способен рассмеяться и увидеть в том или ином событии смешное", answers1),
                Question(2,"Я испытываю бодрость", answers2),
                Question(3,"Мне кажется, что я стал все делать очень медленно", answers3),
                Question(4,"Я не слежу за своей внешностью", answers4),
                Question(5,"Я считаю, что мои дела (занятия, увлечения) могут принести мне чувство удовлетворения", answers5),
                Question(6,"Я могу получить удовольствие от хорошей книги, радио — или телепрограммы", answers6),
        )

        private val result0 = Result(0, 0, 7,"", StringUtills.STRING_DEPRESSION_0)
        private val result1 = Result(1, 8, 10,"",StringUtills.STRING_DEPRESSION_1)
        private val result2 = Result(2, 11, 100,"", StringUtills.STRING_DEPRESSION_2)
        private val results : List<Result> = arrayListOf(result0, result1, result2)

        val entity = EntityTest(id, name,
                "Депрессия   -   психическое   расстройство,   основными   признаками  " +
                        "которого являются сниженное настроение,  снижение или утрата  " +
                        "способности  " + "получать  " +"удовольствие,  " + "появление  " + "заторможенности. ",
                "В каждом вопросе выберите одно из четырех утверждений, которое наиболее вам подходит.",
                image, listQuestions, 0,6,results)
    }


    object TEST_2 {
        val id : Long=2
        val name ="Оценка уровня тревоги"
        val image= R.mipmap.test_image_02

        private val answers0: List<Answer> = arrayListOf(
                Answer(3, text = "Все время"),
                Answer(2, "Часто"),
                Answer(1, "Время от времени"),
                Answer(0, "Совсем не испытываю"))
        private val answers1: List<Answer> = arrayListOf(
                Answer(3, text = "Опеределенно это так"),
                Answer(2, "Да, это так, но страх не очень велик"),
                Answer(1, "Иногда, но это меня не беспокоит"),
                Answer(0, "Совсем не испытываю"))
        private val answers2: List<Answer> = arrayListOf(
                Answer(3, text = "Постоянно"),
                Answer(2, "Большую часть времени"),
                Answer(1, "Время от времени и не так часто"))

        private val answers3: List<Answer> = arrayListOf(
                Answer(3, text = "Совсем не могу"),
                Answer(2, "Лишь изредка, это так"),
                Answer(1, "Наверно, это так"),
                Answer(0, "Определенно, это так  "))
        private val answers4: List<Answer> = arrayListOf(
                Answer(3, text = "Очень часто "),
                Answer(2, "Часто"),
                Answer(1, "Иногда  "),
                Answer(0, "Совсем не испытываю"))
        private val answers5: List<Answer> = arrayListOf(
                Answer(3, text = "Определенно, это так"),
                Answer(2, "Наверное, это так"),
                Answer(1, "Лишь в некоторой степени, это так"),
                Answer(0, "Совсем не испытываю "))

        private val answers6: List<Answer> = arrayListOf(
                Answer(3, text = "Очень часто"),
                Answer(2, "Довольно часто"),
                Answer(1, "Не так уж и часто"),
                Answer(0, "Совсем не бывает"))

        private val listQuestions: List<Question> = arrayListOf(
                Question(0, "Я испытываю напряжение, мне не по себе", answers0),
                Question(1, "Я испытываю страх, кажется, будто что-то ужасное может вот-вот случиться", answers1),
                Question(2, "Беспокойные мысли крутятся у меня в голове", answers2),
                Question(3, "Я легко могу сесть и расслабиться", answers3),
                Question(4, "Я испытываю внутреннее напряжение или дрожь", answers4),
                Question(5, "Я испытываю неусидчивость, мне постоянно нужно двигаться", answers5),
                Question(6, "У меня бывает внезапное чувство паники", answers6),
        )

        private val result0 = Result(0, 0, 7,"", StringUtills.STRING_ANXIETY_0)
        private val result1 = Result(1, 8, 10,"", StringUtills.STRING_ANXIETY_1)
        private val result2 = Result(2, 11, 100,"", StringUtills.STRING_ANXIETY_2)
        private val results : List<Result> = arrayListOf(result0, result1, result2)

        val text ="Тревога –   неприятное,   негативное   эмоциональное   состояние,  " +
                "которое   проявляется   непонятным,   малообъяснимым   ощущением  " +
                "грядущей   опасности,   ожиданием   неприятных   или   угрожающих  " +
                "событий,   дурными   предчувствиями,   смутными   опасениями.  " +
                "Состояние  тревоги   проявляется   в   напряжении   и   беспокойстве.  " +
                "Тревога может предшествовать наступлению депрессии."

        val entity = EntityTest(id, name,
                text,
                "В каждом вопросе выберите одно из четырех утверждений, которое наиболее вам подходит.",
                image, listQuestions,0,6,results)
    }

    object TEST_3{
        val id: Long=3
        val name ="Опросник по качеству жизни пациента"
        val image= R.mipmap.test_image_03

        private val answers0: List<Answer> = arrayListOf(
                Answer(3, "Очень сильно"),
                Answer(2, "Сильно"),
                Answer(1, "Незначительно"),
                Answer(0, "Совсем нет"))

        private val answers1: List<Answer> = arrayListOf(
                Answer(3, "Очень сильно",),
                Answer(2, "Сильно",),
                Answer(1, "Незначительно",),
                Answer(0, "Совсем нет",),
                Answer(ZERO_FLAG,  "Ко мне не относится",  ))

        private val answers2: List<Answer> = arrayListOf(
                Answer(1, "Да",),
                Answer(0, "Нет",),
                Answer(ZERO_FLAG,  "Ко мне не относится"))

        private val answers3: List<Answer> = arrayListOf(
                Answer(2, "Сильно"),
                Answer(1, "Незначительно"),
                Answer(0, "Совсем не влияло"))

        private val listQuestions: List<Question> = arrayListOf(
                Question(0,"На протяжении последней недели, насколько сильно беспокоили Вас зуд, чувствительсть, болезненность или жжение кожи?",answers0),
                Question(1,"На протяжении последней недели, насколько сильно Вы чувствовали смущение или неловкость из-за Вашей кожи?", answers0),
                Question(2,"На протяжении последней недели, насколько сильно состояние Вашей кожи мешало Вашим походам за покупками, уходу за домом или садом?", answers1),
                Question(3,"На протяжении последней недели, насколько сильно состояние Вашей кожи влияло на выбор одежды, которую вы одевали?", answers1),
                Question(4,"На протяжении последней недели, насколько сильно состояние Вашей кожи влияло на Вашу социальную жизнь или досуг?", answers1),
                Question(5,"На протяжении последней недели, насколько сильно состояние Вашей кожи затрудняло Ваши занятия спортом?", answers1),
                Question(6,"Позволяло ли состояние вашей кожи на протяжении последней недели полноценно работать или учиться?", answers2),

                Question(7,"В какой степени на протяжении последней недели состояние Вашей кожи было проблемой для Вашей работы или обучения?", answers3),

                Question(8,"На протяжении последней недели, насколько сильно состояние Вашей кожи создавало проблемы с Вашим партнером(-шей) или Вашими близкими друзьями или родственниками?", answers1),
                Question(9,"На протяжении последней недели, насколько сильно состояние Вашей кожи было причиной Ваших каких бы то ни было сексуальных проблем?",answers1),
                Question(10,"На протяжении последней недели, насколько сильно лечение Вашего кожного заболевания создавало Вам сложности, например, создавало беспорядок в доме или отнимало время?",answers1)
        )

        private val result0 = Result(0, 21, 30,"Кол-во баллов: 21-30. Заболевание оказывает чрезвычайно большое влияние на Вашу жизнь, и нужно как можно скорее обратиться к врачу ","" )
        private val result1 = Result(1, 11, 20,"Кол-во баллов: 11-20. Заболевание оказывает очень большое влияние на Вашу жизнь, что может привести к ухудшению качества жизни. ", "")
        private val result2 = Result(2, 6, 10,"Кол-во баллов: 6-10. Заболевание оказывает умеренное влияние на жизнь пациента", "")
        private val result3 = Result(3, 2, 5,"Кол-во баллов: 2-5. Заболевание оказывает небольшое влияние на Вашу жизнь", "")
        private val result4 = Result(4, 0, 1,"Кол-во баллов: 0-1. Заболевание вообще не влияет на Вашу  жизнь ", "")

        private val results : List<Result> = arrayListOf(result0, result1, result2, result3, result4)

        val entity = EntityTest(id, name,
                "Цель этого опросника - оценить, какое влияние оказывало на Вашу жизнь каждое заболевание НА ПРОТЯЖЕНИИ ПОСЛЕДНЕЙ НЕДЕЛИ. Пожалуйста, отметьте галочкой одну ячейку для каждого вопроса.",
                "В каждом вопросе выберите одно из утверждений, которое наиболее вам подходит.",image, listQuestions, 0, 5,results)

    }
    object TEST_4{
        val id: Long=4
        val name ="Дневник симптоматики псориаза (PSSD)"
        val image= R.mipmap.test_image_04

        private val answers0: List<Answer> = arrayListOf(
                Answer(10, "10"),
                Answer(9, "9"),
                Answer(8, "8"),
                Answer(7, "7"),
                Answer(6, "6"),
                Answer(5, "5"),
                Answer(4, "4"),
                Answer(3, "3"),
                Answer(2, "2"),
                Answer(1, "1"),
                Answer(0, "0"))

        private val listQuestions: List<Question> = arrayListOf(
                Question(0,"Оцените выраженность зуда на протяжении последних 24 часов.", answers0),
                Question(1,"Оцените выраженность сухости кожи на протяжении последних 24 часов.", answers0),
                Question(2,"Оцените выраженность растрескивания кожи на протяжении последних 24 часов.", answers0),
                Question(3,"Оцените выраженность стянутости кожи на протяжении последних 24 часов.", answers0),
                Question(4,"Оцените выраженность образования чешуек (наростов) на коже на протяжении последних 24 часов.", answers0),
                Question(5,"Оцените выраженность шелушения кожи (отслаивания чешуек) на протяжении последних 24 часов.", answers0),
                Question(6,"Оцените выраженность покраснения кожи на протяжении последних 24 часов.", answers0),
                Question(7,"Оцените выраженность кровоточивости на протяжении последних 24 часов.", answers0),
                Question(8,"Оцените выраженность ощущения жжения на протяжении последних 24 часов.", answers0),
                Question(9,"Оцените выраженность ощущения покалывания на протяжении последних 24 часов.", answers0),
                Question(10,"Оцените выраженность болезненности очагов псориаза на протяжении последних 24 часов.", answers0),
        )

        private val result0 = Result(0, 0, 100,"",
                "Заполняя ежедневно дневник, Вы сможете отлеживать эффект от проводимого лечения. Снижении количества набранных баллов по сравнению с " +
                        "предыдущими днями указывает на положительный эффект от лечения, увеличение или отсутствие значительной динамики – может указывать на низкую эффективность " +
                        "проводимого лечения. \n " +
                        "За любой информацией об использовании Дневника симптоматики псориаза (PSSD) обращайтесь в Mapi Research Trust, Lyon, France." )

        private val results : List<Result> = arrayListOf(result0)

        val entity = EntityTest(id, name,
                "Данный дневник предназначен для отслеживания эффективности проводимого лечения. Его рекомендуется " +
                        "заполнять ежедневно (или через день) с начала лечения. Данные вопросы касаются последних 24 часов." ,
                "Пожалуйста, укажите, насколько серьёзным был каждый из перечисленных кожных симптомов в течение " +
                " последних 24 часов."

                , image, listQuestions, 0, 6,results)
    }
}