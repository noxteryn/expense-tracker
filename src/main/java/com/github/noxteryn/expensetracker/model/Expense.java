package com.github.noxteryn.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
public class Expense
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal money;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;
	private String text;
	public Expense()
	{
	}
	public Expense(BigDecimal money, Date date, String text)
	{
		this.money = money;
		this.date = date;
		this.text = text;
	}
	@Override
	public String toString()
	{
		return "Expense{" +
				"id=" + id +
				", money=" + money +
				", date=" + date +
				", text='" + text + '\'' +
				'}';
	}
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public BigDecimal getMoney()
	{
		return money;
	}
	public void setMoney(BigDecimal money)
	{
		this.money = money;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date date)
	{
		this.date = date;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
}
