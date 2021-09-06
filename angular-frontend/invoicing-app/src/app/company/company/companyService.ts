import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Company} from '../../models/company';

const PATH = 'companies';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  private options = {
    headers: new HttpHeaders({'Content-type': 'application/json'}),
    withCredentials: true
  };

  private contentType = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getCompanies(): Observable<Company[]> {
    return this.http.get<Company[]>(this.apiUrl(PATH, -1));
  }

  addCompany(company: Company): Observable<any> {
    return this.http.post<any>(this.apiUrl(PATH, -1), this.toCompanyRequest(company), this.contentType);
  }

  deleteCompany(id: number): Observable<any> {
    return this.http.delete<any>(this.apiUrl(PATH, id));
  }

  editCompany(company: Company): Observable<any> {
    return this.http.put<Company>(this.apiUrl(PATH, company.id), this.toCompanyRequest(company), this.contentType);
  }

  private apiUrl(service: string, id: number): string {
    const idInUrl = (id !== -1 ? '/' + id : '');

    return environment.apiUrl + '/' + service + idInUrl;
  }

  private toCompanyRequest(company: Company) {
    return {
      taxIdentificationNumber: company.taxIdentificationNumber,
      name: company.name,
      address: company.address,
      pensionInsurance: company.pensionInsurance,
      healthInsurance: company.healthInsurance,
    };
  }

}
