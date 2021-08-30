package pl.futurecollars.invoicing.controller.invoice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Company;

@CrossOrigin
@RequestMapping("companies")
@Api(tags = {"company-controller"})
public interface CompanyApi {

    @ApiOperation(value = "Get list of all companies")
    @GetMapping
    ResponseEntity<List<Company>> getAllCompanies();

    @ApiOperation(value = "Add new company to system")
    @PostMapping
    ResponseEntity<Long> saveCompany(@RequestBody Company company);

    @ApiOperation(value = "Get company by id")
    @GetMapping("/{id}")
    ResponseEntity<Company> getCompanyById(@PathVariable Long id);

    @ApiOperation(value = "Update company with given id")
    @PutMapping("/{id}")
    ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody Company company);

    @ApiOperation(value = "Delete company with given id")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteById(@PathVariable Long id);
}
