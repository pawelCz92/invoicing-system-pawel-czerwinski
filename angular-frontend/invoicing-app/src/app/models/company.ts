export class Company {

    public editMode: boolean = false;
    public editedCompany!: Company;
    
    constructor(
        public id: number,
        public taxIdentificationNumber: string,
        public address: string,
        public name: string,
        public healthInsurance: number,
        public pensionInsurance: number
    ) {
    }
}