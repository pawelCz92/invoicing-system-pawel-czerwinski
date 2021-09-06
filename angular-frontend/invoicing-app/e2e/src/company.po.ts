import {browser, by, element, ElementArrayFinder, ElementFinder, WebElement} from 'protractor';

export class CompanyPage {
  async navigateTo(): Promise<unknown> {
    return browser.get(browser.baseUrl);
  }

  async taxIdHeaderValue(): Promise<string> {
    return element(by.id('taxIdHeader')).getText();
  }

  async nameHeaderValue(): Promise<string> {
    return element(by.id('nameHeader')).getText();
  }

  async addressHeaderValue(): Promise<string> {
    return element(by.id('addressHeader')).getText();
  }

  async pensionInsuranceHeaderValue(): Promise<string> {
    return element(by.id('pensionInsuranceHeader')).getText();
  }

  async healthInsuranceHeaderValue(): Promise<string> {
    return element(by.id('healthInsuranceHeader')).getText();
  }

  companyRows(): ElementArrayFinder {
    return element.all(by.css('.companyRow'));
  }

  deleteBtn(row: ElementFinder): WebElement {
    return row.element(by.css('.btn-danger'));
  }

  async addNewCompany(taxId: string, name: string, address: string, healthInsurance: number, pensionInsurance: number) {
    await this.taxIdInput().sendKeys(taxId);
    await this.nameInput().sendKeys(name);
    await this.addressInput().sendKeys(address);

    await this.healthInsuranceInput().clear();
    await this.healthInsuranceInput().sendKeys(healthInsurance);

    await this.pensionInsuranceInput().clear();
    await this.pensionInsuranceInput().sendKeys(pensionInsurance);

    await element(by.id('addCompanyBtn')).click();
  }

  private addressInput() {
    return element(by.css('input[name=address]'));
  }

  private nameInput() {
    return element(by.css('input[name=name]'));
  }

  private taxIdInput() {
    return element(by.css('input[name=taxIdentificationNumber]'));
  }

  private healthInsuranceInput() {
    return element(by.css('input[name=healthInsurance]'));
  }

  private pensionInsuranceInput() {
    return element(by.css('input[name=pensionInsurance]'));
  }
}
