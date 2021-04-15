package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Channels;

public interface ChannelsRepository extends JpaRepository<Channels, String> {

}
