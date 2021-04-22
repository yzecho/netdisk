package io.yzecho.netdisk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @className: PageRequestDTO
 * @description:
 * @author: liuzhe
 * @date: 2021/4/22
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    private Integer page;
    private Integer size;
}
