INSERT INTO public.tariffs (id, title, description) VALUES ('01', 'Общительный', 'Тариф для тех, кто много разговаривает');
INSERT INTO public.tariffs (id, title, description) VALUES ('02', 'Обычный', 'Тариф с небольшим пакетом минут и интернета');
INSERT INTO public.tariffs (id, title, description) VALUES ('03', 'Блогер', 'Тариф для тех, кто любит смотреть и выкладывать видео');
INSERT INTO public.tariffs (id, title, description) VALUES ('04', 'Оптимум', 'Оптимальное количество минут и интернета');
INSERT INTO public.tariffs (id, title, description) VALUES ('05', 'Только Ромашка', 'Бесплатные звонки от любого абонента оператора Ромашка');
INSERT INTO public.tariffs (id, title, description) VALUES ('06', 'Хз', 'Вставить описание');

INSERT INTO public.telecom_operators (title) VALUES ('Ромашка');

INSERT INTO public.call_types (id, type) VALUES ('01', 'Исходящий');
INSERT INTO public.call_types (id, type) VALUES ('02', 'Входящий');

INSERT INTO public.telephony_packages (call_type_id, operator_id, package_of_minutes, package_cost, package_cost_per_minute, extra_package_cost, extra_package_cost_per_minute) VALUES (null, null, 100, '$5.00', false, '$0.10', true);
INSERT INTO public.telephony_packages (call_type_id, operator_id, package_of_minutes, package_cost, package_cost_per_minute, extra_package_cost, extra_package_cost_per_minute) VALUES (null, null, 500, '$20.00', false, '$0.10', true);
INSERT INTO public.telephony_packages (call_type_id, operator_id, package_of_minutes, package_cost, package_cost_per_minute, extra_package_cost, extra_package_cost_per_minute) VALUES (null, 1, 0, '$0.00', false, '$0.00', false);

INSERT INTO public.internet_packages (package_of_mb, package_cost, package_cost_per_mb, extra_package_cost, extra_package_cost_per_mb) VALUES (1000, '$10.00', false, '$0.10', true);
INSERT INTO public.internet_packages (package_of_mb, package_cost, package_cost_per_mb, extra_package_cost, extra_package_cost_per_mb) VALUES (10000, '$40.00', false, '$0.10', true);

INSERT INTO public.tariffs_config (tariff_id, telephony_package_id, internet_package_id) VALUES ('01', 2, null);
INSERT INTO public.tariffs_config (tariff_id, telephony_package_id, internet_package_id) VALUES ('02', 1, 1);
INSERT INTO public.tariffs_config (tariff_id, telephony_package_id, internet_package_id) VALUES ('03', null, 2);
INSERT INTO public.tariffs_config (tariff_id, telephony_package_id, internet_package_id) VALUES ('04', 2, 2);
INSERT INTO public.tariffs_config (tariff_id, telephony_package_id, internet_package_id) VALUES ('05', 3, null);
INSERT INTO public.tariffs_config (tariff_id, telephony_package_id, internet_package_id) VALUES ('06', 1, null);

INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Artem', 'Kurdikov', 'Sergeevich', 21, '2002-08-08');
INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Channa', 'Dongall', '', 49, '1975-12-03');
INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Marjie', 'Symcoxe', '', 37, '1987-07-07');
INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Sal', 'Riste', '', 21, '2003-12-18');
INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Raddy', 'Mehaffey', '', 22, '2001-08-23');
INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Rogers', 'Furmonger', '', 31, '1993-03-25');
INSERT INTO public.clients (first_name, last_name, patronymic, age, birthday) VALUES ('Dawna', 'Renfrew', '', 27, '1997-05-13');

INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (1, 'sriste0@gmail.com', '$2a$12$XwPNVJA/QXPvj9iGKalSc.e9MrMtZacL5w4F3MmbDfHYA16YYisd2', 'RU', '1111111111', '2022-12-01', '1');
INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (2, 'rfurmonger1@gmail.com', '$2a$12$dNcrQo21UBWud1E4EvX6NeLPGt87oIKmIng6UDiR9HGQRSeeHQymu', 'RU', '2222222222', '2022-12-02', '2');
INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (3, 'drenfrew2@gmail.com', '$2a$12$xOCWP.jQhYYVPKSVZh/XDeA3nbRBWktV9pGKETd7vCHaMlLpZkmEa', 'RU', '3333333333', '2022-12-03', '3');
INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (4, 'rmehaffey3@gmail.com', '$2a$12$dHr.K10TB2fYrhZbXN/YtOFuqTD0aB3qq83XWTYSkq7/zQ.sLiPrm', 'RU', '4444444444', '2022-12-04', '4');
INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (5, 'cdongal4@gmail.com', '$2a$12$TnH7k7F5oVzwJupC3w9QfeWXnLpEMxfCVk.wGfdmBL96Uf5XMgoR2', 'RU', '5555555555', '2022-12-05', '5');
INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (6, 'msymcoxe5@gmail.com', '$2a$12$gpVCXJGX14jTAS5WkB/L.uEZwvoW5SOB7FI8zHEVTolQtag10zORa', 'RU', '6666666666', '2022-12-06', '6');
INSERT INTO public.client_details (number_personal_account, email, password, region, passport, contract_date, contract_number) VALUES (7, 'kourtema@gmail.com', '$2a$12$OImLncO0yTHHSnXaCdzv5.3Y.ydwI7GzOfedcT2bBOnictf/N9S7.', 'RU', '7777777777', '2022-12-07', '7');

INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+71111111111', '$1.00', '01');
INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+72222222222', '$2.00', '02');
INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+73333333333', '$3.00', '03');
INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+74444444444', '$4.00', '04');
INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+75555555555', '$5.00', '05');
INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+76666666666', '$6.00', '06');
INSERT INTO public.phone_numbers (phone_number, balance, tariff_id) VALUES ('+77777777777', '$7.00', '01');

INSERT INTO public.users (role) VALUES ('USER');
INSERT INTO public.users (role) VALUES ('USER');
INSERT INTO public.users (role) VALUES ('USER');
INSERT INTO public.users (role) VALUES ('USER');
INSERT INTO public.users (role) VALUES ('USER');
INSERT INTO public.users (role) VALUES ('USER');
INSERT INTO public.users (role) VALUES ('USER');

