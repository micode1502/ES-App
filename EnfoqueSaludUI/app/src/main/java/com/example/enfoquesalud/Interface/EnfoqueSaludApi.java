package com.example.enfoquesalud.Interface;

import com.example.enfoquesalud.Model.Read.Appointment;
import com.example.enfoquesalud.Model.Read.Doctor;
import com.example.enfoquesalud.Model.Register.AppointmentRegister;
import com.example.enfoquesalud.Model.Register.InvoiceRegister;
import com.example.enfoquesalud.Model.Register.NotificationUser;
import com.example.enfoquesalud.Model.Register.UserRegister;
import com.example.enfoquesalud.Model.RequestUser;
import com.example.enfoquesalud.Model.Read.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EnfoqueSaludApi {
    @POST("api/users/login")
    Call<RequestUser> getToken(@Body User user);

    @POST("api/users/signup")
    Call<RequestUser> saveUser(@Body UserRegister userRegister);
    @POST("api/patients/{id}/appointment")
    Call<RequestUser> saveAppointment(@Header("Authorization") String authorization,@Body AppointmentRegister appointmentRegister,
                                              @Path("id") int patienteId);

    @GET("api/doctors")
    Call<List<Doctor>> getListDoctor(@Header("Authorization") String authorization);

    @GET("api/doctors/specialties")
    Call<List<String>> getListSpecialties(@Header("Authorization") String authorization);

    @GET("api/doctors/specialty/{specialty}")
    Call<List<Doctor>> getListDoctorSpecialty(@Header("Authorization") String authorization,
                                                @Path("specialty") String specialty);
    @GET("api/doctors/{id}")
    Call<Doctor> getDoctorDetailsId(@Header("Authorization") String authorization,
                                      @Path("id") int doctorId);

    @GET("api/doctors/full_name/{name}")
    Call<List<Doctor>> searchDoctor(@Header("Authorization") String authorization,
                                            @Path("name") String name);
    @GET("api/patients/{id}/appointments")
    Call<List<Appointment>> getPatientAppointments(@Header("Authorization") String authorization,
                                                   @Path("id") int patientId);
    @POST("api/patients/{id}/invoice")
    Call<RequestUser> savePayment(@Header("Authorization") String authorization,
                                  @Body InvoiceRegister invoiceRegister, @Path("id") int patienteId);
    @GET("api/patients/{id}/notifications")
    Call<List<NotificationUser>> getPatientNotification(@Header("Authorization") String authorization,
                                                        @Path("id") int patientId);

}
