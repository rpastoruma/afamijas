 import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { MemberDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class MembersService {


  constructor(private http: HttpClient,) { }

  getMembers(page : number, size : number, membernumber? : number, name_surnames? : string, documentid? : string, status? : string) {
    let url = ENV.url.members + `/getMembers?page=${page}&size=${size}`;
    if(membernumber) url += '&membernumber=' + membernumber;
    if(name_surnames) url += '&name_surnames=' + name_surnames;
    if(documentid) url += '&documentid=' + documentid;
    if(status) url += '&status=' + status;
    return this.http.get<any>(url, {});
  }

  saveMember(theMember: MemberDTO, is_document_signed : boolean) 
  {
    const form = new FormData();
    if(theMember.id && theMember.id!='') form.append('id', theMember.id);
    form.append('name', theMember.name);
    form.append('surname1', theMember.surname1);
    form.append('email', theMember.email);
    form.append('documentid', theMember.documentid);
    form.append('documenttype', theMember.documenttype);

    if(theMember.surname2 && theMember.surname2!='') form.append('surname2', theMember.surname2);
    if(theMember.phone && theMember.phone!='') form.append('phone', theMember.phone);
    form.append('is_document_signed', is_document_signed.toString());

    if(is_document_signed && theMember.register_document_url_signed && theMember.register_document_url_signed!='')
      form.append('register_document_url', theMember.register_document_url_signed);
    else
    {
      if(theMember.register_document_url && theMember.register_document_url!='') 
        form.append('register_document_url', theMember.register_document_url);
    }


    
    if(theMember.postaladdress && theMember.postaladdress!='') form.append('postaladdress', theMember.postaladdress);
    if(theMember.idcity) form.append('idcity', theMember.idcity.toString());
    if(theMember.idstate) form.append('idstate', theMember.idstate.toString());
    if(theMember.idcountry) form.append('idcountry', theMember.idcountry.toString());
    if(theMember.postalcode && theMember.postalcode!='') form.append('postalcode', theMember.postalcode);
    if(theMember.fee_euros && theMember.fee_euros>0) form.append('fee_euros', theMember.fee_euros.toString());
    if(theMember.fee_period && theMember.fee_period!='') form.append('fee_period', theMember.fee_period);
    if(theMember.fee_payment && theMember.fee_payment!='') form.append('fee_payment', theMember.fee_payment);
    if(theMember.bank_name && theMember.bank_name!='') form.append('bank_name', theMember.bank_name);
    if(theMember.bank_account_holder_fullname && theMember.bank_account_holder_fullname!='') form.append('bank_account_holder_fullname', theMember.bank_account_holder_fullname);
    if(theMember.bank_account_holder_dni && theMember.bank_account_holder_dni!='') form.append('bank_account_holder_dni', theMember.bank_account_holder_dni);
    if(theMember.bank_account_iban && theMember.bank_account_iban!='') form.append('bank_account_iban', theMember.bank_account_iban);

 
    return this.http.post<any>(ENV.url.members + '/saveMember', form);
  }


  unregisterMember(id: string, unregister_reason: string, unregister_document_url: string, is_document_signed: boolean) 
  {
    const form = new FormData();
    form.append('id', id);
    if(unregister_reason) form.append('unregister_reason', unregister_reason);
    if(unregister_document_url && unregister_document_url!='') form.append('unregister_document_url', unregister_document_url);
    if(is_document_signed) form.append('is_document_signed', is_document_signed.toString());

 
    return this.http.post<any>(ENV.url.members + '/unregisterMember', form);
  }
  

  signDocumentRegister(idmember: string, register_document_url_signed: string) {
    const form = new FormData();
    form.append('idmember', idmember);
    form.append('register_document_url_signed', register_document_url_signed);

    return this.http.post<any>(ENV.url.members + '/signDocumentRegister', form);
  }
  

  signDocumentUnRegister(idmember: string, unregister_document_url_signed: string) {
    const form = new FormData();
    form.append('idmember', idmember);
    form.append('unregister_document_url_signed', unregister_document_url_signed);

    return this.http.post<any>(ENV.url.members + '/signDocumentUnRegister', form);
  }



}
