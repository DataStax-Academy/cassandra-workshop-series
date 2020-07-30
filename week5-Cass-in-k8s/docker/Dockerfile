FROM python:slim
COPY . /
RUN pip install -r requirements.txt
EXPOSE 80
CMD python -m http.server 80
