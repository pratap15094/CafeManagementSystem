import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  url = environment.apiurl;

  constructor(private httpClent: HttpClient) { }

  add(data: any) {
    return this.httpClent.post(this.url + "/category/add", data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    })
  }

  update(data: any) {
    return this.httpClent.post(this.url + "/category/update", data, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    })
  }

  getCategory(){
    return this.httpClent.get(this.url + "/category/get");
  }
}
