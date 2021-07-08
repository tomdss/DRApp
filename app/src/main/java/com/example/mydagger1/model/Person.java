package com.example.mydagger1.model;

import javax.inject.Inject;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

//@Module
public class Person {

    private Pet pet;

    // C1 Không tuân thủ theo IoC(không 1 class nào đc khởi tạo đối tượng bên trong nó)
    /*public Person() {
        this.pet = new Pet();
    }*/

    // C2 Nhúng(Inject) pet từ 1 nguồn khác -> Person chỉ cần sử dụng pet khi cần thiết
    public Person(Pet pet){
        this.pet = pet;
    }

    /*
    // ->Dependency Injection(DI) sẽ giúp khởi tạo Pet
    // example dagger
    @Provides
    public Pet providePet(){
        return new Pet();
    }
    // component ...
    // in class use model Pet, as Person
    @Inject
    Pet pet1;
    */

}
