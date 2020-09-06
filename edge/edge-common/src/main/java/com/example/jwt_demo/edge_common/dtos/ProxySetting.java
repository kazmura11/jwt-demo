package com.example.jwt_demo.edge_common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxySetting {
	private String host;
	private int port;
}
