import {TestBed} from '@angular/core/testing';
import {CompanyComponent} from './company.component';
import {FormsModule} from '@angular/forms';
import {CompanyService} from './companyService';
import {Company} from '../../models/company';
import {Observable, of} from 'rxjs';


describe('CompanyComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: CompanyService, useClass: MockCompanyService}
      ],
      declarations: [
        CompanyComponent
      ],
      imports: [FormsModule]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(CompanyComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  // it(`should have as title 'invoicing-app'`, () => {
  //   const fixture = TestBed.createComponent(CompanyComponent);
  //   const app = fixture.componentInstance;
  //   expect(app.title).toEqual('invoicing-app');
  // });

//   it('should render title', () => {
//     const fixture = TestBed.createComponent(CompanyComponent);
//     fixture.detectChanges();
//     const compiled = fixture.nativeElement;
//     expect(compiled.querySelector('h2.titleHeader').textContent).toContain('Company');
//   });
// });

  class MockCompanyService {
    companies: Company[] = [
      new Company(
        1,
        '22-143-12-44',
        'ul. Dluga 1',
        'Sony.',
        12.121,
        22122.12
      ),
      new Company(
        2,
        '244-123-89-21',
        'ul. Krótka  2',
        'jbl',
        12122.22,
        333.22
      )
    ];

    getCompanies(): Observable<Company[]> {
      return of(this.companies);
    }
  }
});
