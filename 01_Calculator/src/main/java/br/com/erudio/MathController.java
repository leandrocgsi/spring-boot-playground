package br.com.erudio;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.math.SimpleMath;

@RestController
public class MathController {

    private SimpleMath math = new SimpleMath();
    
    @RequestMapping("/sum")
    public Float sum(@PathVariable("numberOne")  String numberOne, @PathVariable("numberTwo")  String numberTwo) {
        if (IsNumeric(numberOne) && IsNumeric(numberTwo))
        {
            Float sum = math.sum(CovertToDecimal(numberOne), CovertToDecimal(numberTwo));
            return sum;
        }

        return ;
    }
    
    private Float CovertToDecimal(String number)
    {
        if (IsNumeric(number)) return Float.parseFloat(number);
        return 0f;
    }

    private boolean IsNumeric(String strNumber)
    {
        return StringUtils.isNumeric(strNumber);
    }
}