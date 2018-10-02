package br.com.erudio;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {

    @RequestMapping("/sum")
    public Float sum(@PathVariable("numberOne")  String numberOne, @PathVariable("numberTwo")  String numberTwo) {
        return 10.5f;
    }
    
    
}