
package com.bidderApp.bidz.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specification {

    String specificationName;
    String value;
}
