package com.iscas.graph;

import com.iscas.graph.dao.PersonRepository;
import com.iscas.graph.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.OpenOption;
import java.util.Optional;

@SpringBootTest
class GraphApplicationTests {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void testCreate() {
        // Optional<Person> 用于解决空指针异常的问题

        Optional<Person> byId = personRepository.findById(2263L);
        byId.orElse(null);

        personRepository.deleteById(73L);

        // 增加节点
        Person person = new Person();
        person.setName("玉皇大地");
        // 创建节点
//        PersonRepository.save(person);



    }

}
