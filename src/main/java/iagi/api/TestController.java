package iagi.api;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author is
 */
@RestController
@RequestMapping(path = "/test")
public class TestController {

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public Map get(@RequestBody Map msg) {
        return null;
    }

}
