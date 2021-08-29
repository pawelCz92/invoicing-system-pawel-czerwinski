import { Component, OnInit } from '@angular/core';
import { Company } from 'src/app/models/company';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css']
})
export class CompanyComponent implements OnInit {

  newCompany: Company = new Company("", "", "", 0, 0);

  constructor() { }

  companies: Company[] = [

    new Company(
      "421-896-78-55",
      "18674 West Avenue",
      "JPL",
      435,
      332
    ),
    new Company(
      "333-896-78-22",
      "377 Ohio Road Pulo",
      "Voolith",
      800,
      400
    )
  ]

  ngOnInit(): void {
  }

  addCompany() {
    this.companies.push(this.newCompany);
    this.newCompany = new Company("", "", "", 0, 0);
  }

  deleteCompany(companyToDelete: Company) {
    this.companies = this.companies.filter(company => company !== companyToDelete);
  }

  triggerUpdate(company: Company) {
    company.editedCompany = new Company(
      company.taxIdentificationNumber,
      company.address,
      company.name,
      company.healthInsurance,
      company.pensionInsurance
    )

    company.editMode = true
  }

  cancelCompanyUpdate(company: Company) {
    company.editMode = false;
  }

  updateCompany(updatedCompany: Company) {
    updatedCompany.taxIdentificationNumber = updatedCompany.editedCompany.taxIdentificationNumber
    updatedCompany.address = updatedCompany.editedCompany.address
    updatedCompany.name = updatedCompany.editedCompany.name
    updatedCompany.healthInsurance = updatedCompany.editedCompany.healthInsurance
    updatedCompany.pensionInsurance = updatedCompany.editedCompany.pensionInsurance

    updatedCompany.editMode = false;
  }

}

