module com.example.caronas {
    requires javafx.controls;
    requires java.base;

    exports com.example.caronas;
    exports com.example.caronas.model;
    exports com.example.caronas.view;
    exports com.example.caronas.controller;
    exports com.example.caronas.service;
    exports com.example.caronas.repository;
    exports com.example.caronas.util;
    exports com.example.caronas.strategy;
    exports com.example.caronas.state;
    exports com.example.caronas.event;
    exports com.example.caronas.facade;
}