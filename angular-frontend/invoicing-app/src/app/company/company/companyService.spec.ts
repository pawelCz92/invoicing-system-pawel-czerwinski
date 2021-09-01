import {TestBed} from '@angular/core/testing';
import {CompanyService} from './companyService';
import {Company} from '../../models/company';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {environment} from '../../../environments/environment';

describe('CompanyService', () => {

    let httpTestingController: HttpTestingController;
    let companyService: CompanyService;

    beforeEach(async () => {
      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule
        ]
      });

      httpTestingController = TestBed.inject(HttpTestingController);
      companyService = TestBed.inject(CompanyService);
    });

    it(`calling getCompanies() should invoke GET companies`, () => {
      companyService.getCompanies().subscribe(companies => expect(companies).toEqual(expectedCompanies));

      const request = httpTestingController.expectOne(`${environment.apiUrl}/companies`);
      expect(request.request.method).toBe('GET');

      request.flush(expectedCompanies);

      httpTestingController.verify();
    });

    it(`calling addCompany() should invoke POST`, () => {
      const company = expectedCompanies[0];
      const expectedId = 99;

      companyService.addCompany(company).subscribe(id => expect(id).toEqual(expectedId));

      const request = httpTestingController.expectOne(`${environment.apiUrl}/companies`);
      expect(request.request.method).toBe('POST');
      expect(request.request.body).toEqual(
        {
          taxIdentificationNumber: '121-222-33-55',
          name: 'Sony',
          address: 'ul. Dluga 3',
          healthInsurance: 44.44,
          pensionInsurance: 323.44
        }
      );

      request.flush(expectedId);

      httpTestingController.verify();
    });

    const expectedCompanies: Company[] = [
      new Company(
        1,
        '121-222-33-55',
        'ul. Dluga 3',
        'Sony',
        44.44,
        323.44
      ),
      new Company(
        2,
        '234-223-44-55',
        'ul. Krotka 42',
        'Panasonic',
        322.45,
        434.33
      )
    ];
  });
